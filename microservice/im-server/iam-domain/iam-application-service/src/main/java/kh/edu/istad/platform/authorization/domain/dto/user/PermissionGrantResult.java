package kh.edu.istad.platform.authorization.domain.dto.user;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PermissionGrantResult(
        UUID userUuid,
        UUID permissionUuid,
        String permissionName,
        Instant grantedAt,
        String grantedBy,
        String reason
) {
}

