package kh.edu.istad.platform.authorization.web.controller;

import kh.edu.istad.platform.authorization.web.dto.ConsentView;
import kh.edu.istad.platform.authorization.web.service.ConsentWebService;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class AuthorizationConsentController {

    private final ConsentWebService consentWebService;

    public AuthorizationConsentController(ConsentWebService consentWebService) {
        this.consentWebService = consentWebService;
    }

    @GetMapping("/oauth2/consent")
    public String consent(
            Principal principal,
            Model model,
            @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
            @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
            @RequestParam(OAuth2ParameterNames.STATE) String state,
            @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode
    ) {
        ConsentView consentView = consentWebService.prepareConsent(clientId, scope, state, userCode, principal.getName());

        model.addAttribute("clientId", consentView.clientId());
        model.addAttribute("clientName", consentView.clientName());
        model.addAttribute("state", consentView.state());
        model.addAttribute("scopes", consentView.scopes());
        model.addAttribute("previouslyApprovedScopes", consentView.previouslyApprovedScopes());
        model.addAttribute("principalName", consentView.principalName());
        model.addAttribute("userCode", consentView.userCode());
        model.addAttribute("requestURI", consentView.requestURI());

        return "consent";
    }
}
