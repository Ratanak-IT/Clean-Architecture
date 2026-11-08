package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record GrantPermissionsToUserRequest(
        @NotEmpty Set<UUID> permissionUuids,
        String effect,
        String reason
) {
}
