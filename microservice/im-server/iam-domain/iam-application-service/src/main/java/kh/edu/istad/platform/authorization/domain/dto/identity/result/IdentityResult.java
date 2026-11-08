package kh.edu.istad.platform.authorization.domain.dto.identity.result;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record IdentityResult(
        UUID uuid,
        String provider,
        String email,
        String displayName,
        Instant linkedAt
) {
}
