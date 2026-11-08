package kh.edu.istad.platform.authorization.restapi.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record RoleAssignmentResponse(
        UUID userUuid,
        UUID roleUuid,
        String roleName,
        Instant assignedAt,
        String assignedBy,
        String reason
) {
}

