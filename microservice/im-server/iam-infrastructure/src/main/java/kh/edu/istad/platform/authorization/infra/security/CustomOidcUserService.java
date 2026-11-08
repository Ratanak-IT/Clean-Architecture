package kh.edu.istad.platform.authorization.infra.security;

import kh.edu.istad.platform.authorization.domain.dto.identity.command.FederatedUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.identity.result.AuthUserResult;
import kh.edu.istad.platform.authorization.domain.usecase.identity.FindOrProvisionFederatedUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String accessToken = userRequest.getAccessToken().getTokenValue();
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        String providerSubject = oidcUser.getAttribute("sub");
        String email = oidcUser.getAttribute("email");
        String name = oidcUser.getAttribute("name");
        String pictureUrl = oidcUser.getAttribute("picture");
        String username = (email != null && !email.isBlank()) ? email.split("@")[0] : "google_" + providerSubject;

        FederatedUserCommand command = FederatedUserCommand.builder()
                .provider(provider)
                .providerSubject(providerSubject)
                .email(email)
                .name(name)
                .username(username)
                .pictureUrl(pictureUrl)
                .accessToken(accessToken)
                .build();

        AuthUserResult authUser = findOrProvisionFederatedUserUseCase.execute(command);
        log.info("Federated OIDC login succeeded for user: {} with roles: {}", authUser.username(), authUser.roles());

        return oidcUser;
    }
}

