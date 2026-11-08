package kh.edu.istad.platform.authorization.restapi.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PermissionGrantResponse(
        UUID userUuid,
        UUID permissionUuid,
        String permissionName,
        Instant grantedAt,
        String grantedBy,
        String reason
) {
}

