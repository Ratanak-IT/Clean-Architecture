package kh.edu.istad.platform.authorization.domain.dto.role.result;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record RoleSummary(
        UUID uuid,
        String name,
        String description,
        boolean system,
        boolean active,
        int permissionCount,
        Instant createdAt
) {
}

