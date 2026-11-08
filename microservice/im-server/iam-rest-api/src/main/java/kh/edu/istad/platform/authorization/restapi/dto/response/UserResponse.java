package kh.edu.istad.platform.authorization.restapi.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID uuid,
        String username,
        String email,
        boolean emailVerified,
        String name,
        String locale,
        boolean enabled,
        List<RoleSummaryResponse> roles,
        Instant createdAt,
        Instant updatedAt
) {
    @Builder
    public record RoleSummaryResponse(
            UUID uuid,
            String name
    ) {
    }
}

