package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ResetUserPasswordRequest(
        @NotBlank String newPassword
) {
}

