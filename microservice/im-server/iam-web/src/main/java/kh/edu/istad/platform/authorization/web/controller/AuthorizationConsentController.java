package kh.edu.istad.platform.authorization.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthorizationConsentController {

    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationConsentService authorizationConsentService;

    @GetMapping("/oauth2/consent")
    public String consent(
            Principal principal,
            Model model,
            @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
            @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
            @RequestParam(OAuth2ParameterNames.STATE) String state,
            @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode
    ) {
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new IllegalArgumentException("OAuth2 Client not found: " + clientId);
        }

        OAuth2AuthorizationConsent currentAuthorizationConsent =
                this.authorizationConsentService.findById(registeredClient.getId(), principal.getName());

        Set<String> authorizedScopes = (currentAuthorizationConsent != null)
                ? currentAuthorizationConsent.getScopes()
                : Collections.emptySet();

        Set<String> scopesToApprove = new LinkedHashSet<>();
        Set<String> previouslyApprovedScopes = new LinkedHashSet<>();

        for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
            if (OidcScopes.OPENID.equals(requestedScope)) {
                continue; // OPENID is standard protocol scope and doesn't require explicit consent checkbox
            }
            if (authorizedScopes.contains(requestedScope)) {
                previouslyApprovedScopes.add(requestedScope);
            } else {
                scopesToApprove.add(requestedScope);
            }
        }

        model.addAttribute("clientId", clientId);
        model.addAttribute("clientName", registeredClient.getClientName() != null ? registeredClient.getClientName() : clientId);
        model.addAttribute("state", state);
        model.addAttribute("scopes", withDescription(scopesToApprove));
        model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("userCode", userCode);

        if (StringUtils.hasText(userCode)) {
            model.addAttribute("requestURI", "/oauth2/device_verification");
        } else {
            model.addAttribute("requestURI", "/oauth2/authorize");
        }

        return "consent";
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

