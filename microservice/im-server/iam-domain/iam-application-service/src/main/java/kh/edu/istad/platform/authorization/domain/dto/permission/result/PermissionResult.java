package kh.edu.istad.platform.authorization.domain.dto.permission.result;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PermissionResult(
        UUID uuid,
        String name,
        String resource,
        String action,
        String description,
        boolean system,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}

