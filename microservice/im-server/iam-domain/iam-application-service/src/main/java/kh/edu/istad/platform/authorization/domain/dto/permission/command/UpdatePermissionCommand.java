package kh.edu.istad.platform.authorization.domain.dto.permission.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdatePermissionCommand(
        UUID permissionUuid,
        String description
) {
    public UpdatePermissionCommand {
        description = description == null ? null : description.trim();
    }
}

