package kh.edu.istad.platform.authorization.domain.dto.user;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record AssignRolesToUserCommand(
        UUID userUuid,
        Set<UUID> roleUuids,
        String assignedBy,
        String reason
) {
}

