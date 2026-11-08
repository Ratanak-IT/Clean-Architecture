package kh.edu.istad.platform.authorization.domain.dto.user.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RevokePermissionFromUserCommand(
        UUID userUuid,
        UUID permissionUuid,
        String revokedBy,
        String reason
) {
}

