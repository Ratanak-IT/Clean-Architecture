package kh.edu.istad.platform.authorization.restapi.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PermissionAssignmentResponse(
        UUID roleUuid,
        UUID permissionUuid,
        String permissionName,
        Instant assignedAt,
        String assignedBy,
        String reason
) {
}

