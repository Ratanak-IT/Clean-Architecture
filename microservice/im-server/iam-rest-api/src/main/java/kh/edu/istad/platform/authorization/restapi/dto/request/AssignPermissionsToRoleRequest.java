package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record AssignPermissionsToRoleRequest(
        @NotEmpty(message = "permissionUuids must not be empty")
        Set<UUID> permissionUuids,

        @Size(max = 255, message = "Reason must not exceed 255 characters")
        String reason
) {
}

