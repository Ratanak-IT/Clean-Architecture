package kh.edu.istad.platform.authorization.restapi.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record IdentityResponse(
        UUID uuid,
        String provider,
        String email,
        String displayName,
        Instant linkedAt
) {
}
