package kh.edu.istad.platform.authorization.infra.security;

import kh.edu.istad.platform.authorization.domain.dto.identity.FederatedUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.identity.AuthUserResult;
import kh.edu.istad.platform.authorization.domain.usecase.identity.FindOrProvisionFederatedUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2Service extends DefaultOAuth2UserService {

    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Allow Spring Security to exchange token and load profile
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. Extract access token and provider name
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        // 3. Extract provider attributes
        String providerSubject;
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String username;
        String pictureUrl;

        if ("GOOGLE".equals(provider)) {
            providerSubject = oAuth2User.getAttribute("sub");
            pictureUrl = oAuth2User.getAttribute("picture");
            username = (email != null) ? email.split("@")[0] : "google_" + providerSubject;
        } else {
            Object idObj = oAuth2User.getAttribute("id");
            providerSubject = (idObj != null) ? idObj.toString() : null;
            username = oAuth2User.getAttribute("login");
            pictureUrl = oAuth2User.getAttribute("avatar_url");
        }

        // 4. Construct Command
        FederatedUserCommand command = FederatedUserCommand.builder()
                .provider(provider)
                .providerSubject(providerSubject)
                .email(email)
                .name(name)
                .username(username)
                .pictureUrl(pictureUrl)
                .accessToken(accessToken)
                .build();

        // 5. Delegate to UseCase and receive AuthUserResult
        AuthUserResult authUser = findOrProvisionFederatedUserUseCase.execute(command);
        log.info("Federated login succeeded for user: {} with roles: {}", authUser.username(), authUser.roles());

        return oAuth2User;
    }
}
