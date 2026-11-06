package kh.edu.istad.platform.authorization.domain.dto.user;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserSummary(
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

