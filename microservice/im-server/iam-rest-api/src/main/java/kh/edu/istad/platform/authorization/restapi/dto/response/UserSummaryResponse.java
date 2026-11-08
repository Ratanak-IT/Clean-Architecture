package kh.edu.istad.platform.authorization.restapi.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserSummaryResponse(
        UUID uuid,
        String username,
        String email,
        String givenName,
        String familyName,
        String pictureUrl,
        boolean enabled,
        boolean emailVerified,
        Instant createdAt,
        Instant lastLoginAt
) {
}

