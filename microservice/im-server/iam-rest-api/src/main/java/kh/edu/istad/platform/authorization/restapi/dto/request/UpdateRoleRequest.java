package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateRoleRequest(
        @Size(max = 50, message = "Role name must not exceed 50 characters")
        @Pattern(regexp = "^[A-Z0-9_]+$", message = "Role name must be uppercase alphanumeric with underscores")
        String name,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description
) {
}

