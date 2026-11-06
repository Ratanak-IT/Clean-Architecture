package kh.edu.istad.platform.authorization.domain.dto.user;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record GrantPermissionsToUserCommand(
        UUID userUuid,
        Set<UUID> permissionUuids,
        String grantedBy,
        String reason
) {
}

