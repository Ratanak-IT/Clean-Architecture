package kh.edu.istad.platform.authorization.domain.dto.role.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateRoleCommand(
        UUID roleUuid,
        String name,
        String description
) {
}

