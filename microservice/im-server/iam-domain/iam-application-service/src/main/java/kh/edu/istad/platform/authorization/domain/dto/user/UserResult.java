package kh.edu.istad.platform.authorization.domain.dto.user;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record UserResult(
        UUID uuid,
        String username,
        String email,
        boolean emailVerified,
        String name,
        String locale,
        boolean enabled,
        List<RoleSummary> roles,
        Instant createdAt,
        Instant updatedAt
) {
    @Builder
    public record RoleSummary(
            UUID uuid,
            String name
    ) {
    }
}
