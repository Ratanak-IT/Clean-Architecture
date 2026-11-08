package kh.edu.istad.platform.authorization.web.dto;

import java.util.Set;

public record ConnectedAppView(
        String registeredClientId,
        String clientId,
        String clientName,
        Set<String> scopes
) {
}

