package kh.edu.istad.platform.authorization.domain.dto.user.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RevokeRoleFromUserCommand(
        UUID userUuid,
        UUID roleUuid,
        String revokedBy,
        String reason
) {
}

