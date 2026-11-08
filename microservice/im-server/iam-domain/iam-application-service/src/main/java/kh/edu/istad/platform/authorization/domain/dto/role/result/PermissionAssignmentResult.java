package kh.edu.istad.platform.authorization.domain.dto.role.result;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PermissionAssignmentResult(
        UUID roleUuid,
        UUID permissionUuid,
        String permissionName,
        Instant assignedAt,
        String assignedBy,
        String reason
) {
}

