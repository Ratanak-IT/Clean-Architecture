package kh.edu.istad.platform.authorization.web.service;

import kh.edu.istad.platform.authorization.domain.dto.identity.result.IdentityResult;
import kh.edu.istad.platform.authorization.domain.dto.user.command.ChangePasswordCommand;
import kh.edu.istad.platform.authorization.domain.dto.user.command.UpdateUserProfileCommand;
import kh.edu.istad.platform.authorization.domain.dto.user.result.UserResult;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import kh.edu.istad.platform.authorization.domain.usecase.identity.GetMyIdentitiesUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.identity.UnlinkMyIdentityUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.user.ChangePasswordUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.user.GetMyProfileUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.user.UpdateUserProfileUseCase;
import kh.edu.istad.platform.authorization.web.dto.ConnectedAppView;
import kh.edu.istad.platform.authorization.web.dto.UserProfileView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ProfileWebService {

    private static final Logger log = LoggerFactory.getLogger(ProfileWebService.class);

    private final GetMyProfileUseCase getMyProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final GetMyIdentitiesUseCase getMyIdentitiesUseCase;
    private final UnlinkMyIdentityUseCase unlinkMyIdentityUseCase;
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationConsentService authorizationConsentService;
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public ProfileWebService(
            GetMyProfileUseCase getMyProfileUseCase,
            UpdateUserProfileUseCase updateUserProfileUseCase,
            ChangePasswordUseCase changePasswordUseCase,
            GetMyIdentitiesUseCase getMyIdentitiesUseCase,
            UnlinkMyIdentityUseCase unlinkMyIdentityUseCase,
            RegisteredClientRepository registeredClientRepository,
            OAuth2AuthorizationConsentService authorizationConsentService,
            JdbcTemplate jdbcTemplate,
            UserRepository userRepository
    ) {
        this.getMyProfileUseCase = getMyProfileUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.getMyIdentitiesUseCase = getMyIdentitiesUseCase;
        this.unlinkMyIdentityUseCase = unlinkMyIdentityUseCase;
        this.registeredClientRepository = registeredClientRepository;
        this.authorizationConsentService = authorizationConsentService;
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    public UserProfileView getMyProfile() {
        UserResult userResult = getMyProfileUseCase.execute();
        if (userResult == null || userResult.uuid() == null) {
            return null;
        }

        User u = userRepository.findByUuid(userResult.uuid());
        if (u == null) {
            return null;
        }

        return new UserProfileView(
                u.getUsername() != null ? u.getUsername().value() : "",
                u.getEmail() != null ? u.getEmail().value() : "",
                u.getGivenName(),
                u.getFamilyName(),
                u.getNickname(),
                u.getPhoneNumber() != null ? u.getPhoneNumber().value() : "",
                u.getLocale()
        );
    }

    public void updateProfile(String givenName, String familyName, String nickname, String phoneNumber, String locale) {
        UpdateUserProfileCommand command = UpdateUserProfileCommand.builder()
                .givenName(StringUtils.hasText(givenName) ? givenName.trim() : null)
                .familyName(StringUtils.hasText(familyName) ? familyName.trim() : null)
                .nickname(StringUtils.hasText(nickname) ? nickname.trim() : null)
                .phoneNumber(StringUtils.hasText(phoneNumber) ? phoneNumber.trim() : null)
                .locale(StringUtils.hasText(locale) ? locale.trim() : null)
                .build();

        updateUserProfileUseCase.execute(command);
    }

    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        ChangePasswordCommand command = ChangePasswordCommand.builder()
                .currentPassword(currentPassword)
                .newPassword(newPassword)
                .confirmPassword(confirmPassword)
                .build();

        changePasswordUseCase.execute(command);
    }

    public List<IdentityResult> getMyIdentities() {
        try {
            return getMyIdentitiesUseCase.execute();
        } catch (Exception ex) {
            log.warn("Could not retrieve user identities", ex);
            return Collections.emptyList();
        }
    }

    public void unlinkIdentity(UUID id) {
        unlinkMyIdentityUseCase.execute(id);
    }

    public List<ConnectedAppView> getConnectedApps(String principalName) {
        List<ConnectedAppView> apps = new ArrayList<>();
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT registered_client_id, authorities FROM oauth2_authorization_consent WHERE principal_name = ?",
                    principalName
            );

            for (Map<String, Object> row : rows) {
                String regClientId = (String) row.get("registered_client_id");
                String authorities = (String) row.get("authorities");

                RegisteredClient client = registeredClientRepository.findById(regClientId);
                String clientName = (client != null && client.getClientName() != null)
                        ? client.getClientName()
                        : regClientId;
                String clientId = (client != null) ? client.getClientId() : regClientId;

                Set<String> scopes = StringUtils.hasText(authorities)
                        ? Set.of(authorities.split(","))
                        : Collections.emptySet();

                apps.add(new ConnectedAppView(regClientId, clientId, clientName, scopes));
            }
        } catch (Exception ex) {
            log.warn("Could not query authorization consents for principal {}", principalName, ex);
        }
        return apps;
    }

    public void revokeAppConsent(String registeredClientId, String principalName) {
        OAuth2AuthorizationConsent consent = authorizationConsentService.findById(registeredClientId, principalName);
        if (consent != null) {
            authorizationConsentService.remove(consent);
        }
    }
}

