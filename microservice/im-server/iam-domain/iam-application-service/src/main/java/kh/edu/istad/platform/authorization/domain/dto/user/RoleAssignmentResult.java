package kh.edu.istad.platform.authorization.domain.dto.user;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record RoleAssignmentResult(
        UUID userUuid,
        UUID roleUuid,
        String roleName,
        Instant assignedAt,
        String assignedBy,
        String reason
) {
}

