package kh.edu.istad.platform.authorization.domain.dto.user.command;

import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record AssignRolesToUserCommand(
        UUID userUuid,
        Set<UUID> roleUuids,
        Instant expiresAt,
        String assignedBy,
        String reason
) {
}
