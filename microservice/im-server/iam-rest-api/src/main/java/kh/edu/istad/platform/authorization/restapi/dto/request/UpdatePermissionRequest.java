package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdatePermissionRequest(
        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description
) {
}

