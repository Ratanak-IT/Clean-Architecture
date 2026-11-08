package kh.edu.istad.platform.authorization.domain.dto.role.command;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record CreateRoleCommand(
        String name,
        String description,
        Set<UUID> permissionUuids
) {
}

