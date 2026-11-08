package kh.edu.istad.platform.authorization.domain.dto.role.result;

import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record RoleResult(
        UUID uuid,
        String name,
        String description,
        boolean system,
        boolean active,
        Set<PermissionSummary> permissions,
        Instant createdAt,
        Instant updatedAt
) {
    @Builder
    public record PermissionSummary(
            UUID uuid,
            String name,
            String resource,
            String action
    ) {
    }
}
