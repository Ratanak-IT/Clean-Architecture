package kh.edu.istad.platform.authorization.domain.dto.identity.command;

import lombok.Builder;

@Builder
public record FederatedIdentityCommand(
        String provider,
        String providerSubject,
        String email,
        String name,
        String username,
        String pictureUrl,
        String accessToken
) {
}

