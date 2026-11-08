package kh.edu.istad.platform.authorization.domain.dto.role.command;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record AssignPermissionsToRoleCommand(
        UUID roleUuid,
        Set<UUID> permissionUuids,
        String assignedBy,
        String reason
) {
}

