package kh.edu.istad.platform.authorization.web.dto;

import kh.edu.istad.platform.authorization.web.service.ConsentWebService.ScopeWithDescription;
import java.util.Set;

public record ConsentView(
        String clientId,
        String clientName,
        String state,
        String userCode,
        String principalName,
        String requestURI,
        Set<ScopeWithDescription> scopes,
        Set<ScopeWithDescription> previouslyApprovedScopes
) {
}

