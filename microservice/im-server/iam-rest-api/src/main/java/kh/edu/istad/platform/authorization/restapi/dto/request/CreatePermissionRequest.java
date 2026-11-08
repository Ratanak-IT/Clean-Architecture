package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreatePermissionRequest(
        @NotBlank(message = "Resource is required")
        @Size(max = 50, message = "Resource must not exceed 50 characters")
        @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]*$", message = "Resource must use letters, digits, hyphens, or underscores")
        String resource,

        @NotBlank(message = "Action is required")
        @Size(max = 50, message = "Action must not exceed 50 characters")
        @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]*$", message = "Action must use letters, digits, hyphens, or underscores")
        String action,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description
) {
}

