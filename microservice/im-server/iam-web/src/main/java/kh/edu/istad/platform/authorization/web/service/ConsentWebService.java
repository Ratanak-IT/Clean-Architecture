package kh.edu.istad.platform.authorization.web.service;

import kh.edu.istad.platform.authorization.web.dto.ConsentView;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ConsentWebService {

    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationConsentService authorizationConsentService;

    public ConsentWebService(
            RegisteredClientRepository registeredClientRepository,
            OAuth2AuthorizationConsentService authorizationConsentService
    ) {
        this.registeredClientRepository = registeredClientRepository;
        this.authorizationConsentService = authorizationConsentService;
    }

    public ConsentView prepareConsent(String clientId, String scope, String state, String userCode, String principalName) {
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new IllegalArgumentException("OAuth2 Client not found: " + clientId);
        }

        OAuth2AuthorizationConsent currentAuthorizationConsent =
                this.authorizationConsentService.findById(registeredClient.getId(), principalName);

        Set<String> authorizedScopes = (currentAuthorizationConsent != null)
                ? currentAuthorizationConsent.getScopes()
                : Collections.emptySet();

        Set<String> scopesToApprove = new LinkedHashSet<>();
        Set<String> previouslyApprovedScopes = new LinkedHashSet<>();

        for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
            if (OidcScopes.OPENID.equals(requestedScope)) {
                continue;
            }
            if (authorizedScopes.contains(requestedScope)) {
                previouslyApprovedScopes.add(requestedScope);
            } else {
                scopesToApprove.add(requestedScope);
            }
        }

        String clientName = registeredClient.getClientName() != null ? registeredClient.getClientName() : clientId;
        String requestURI = StringUtils.hasText(userCode) ? "/oauth2/device_verification" : "/oauth2/authorize";

        return new ConsentView(
                clientId,
                clientName,
                state,
                userCode,
                principalName,
                requestURI,
                withDescription(scopesToApprove),
                withDescription(previouslyApprovedScopes)
        );
    }

    private static Set<ScopeWithDescription> withDescription(Set<String> scopes) {
        Set<ScopeWithDescription> scopeWithDescriptions = new LinkedHashSet<>();
        for (String s : scopes) {
            scopeWithDescriptions.add(new ScopeWithDescription(s));
        }
        return scopeWithDescriptions;
    }

    public record ScopeWithDescription(String scope, String description) {
        private static final String DEFAULT_DESCRIPTION = "Access authorization permissions for this scope.";
        private static final Map<String, String> SCOPE_DESCRIPTIONS = Map.of(
                OidcScopes.PROFILE, "Access your personal profile information (name, username, avatar).",
                OidcScopes.EMAIL, "View and verify your registered email address.",
                OidcScopes.PHONE, "Access your registered contact phone number.",
                OidcScopes.ADDRESS, "Access your location and postal address information."
        );

        public ScopeWithDescription(String scope) {
            this(scope, SCOPE_DESCRIPTIONS.getOrDefault(scope, DEFAULT_DESCRIPTION));
        }
    }
}

