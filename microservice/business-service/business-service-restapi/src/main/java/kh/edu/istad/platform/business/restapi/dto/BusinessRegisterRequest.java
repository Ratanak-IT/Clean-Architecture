package kh.edu.istad.platform.business.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BusinessRegisterRequest(
        @NotBlank(message = "legalName is required")
        @Size(max = 100, message = "legalName must not exceed 100 characters")
        String legalName,
        @NotBlank(message = "displayName is required")
        @Size(max = 100, message = "displayName must not exceed 100 characters")
        String displayName,
        @NotBlank(message = "email is required")
        @Email(message = "email is not valid")
        @Size(max = 100, message = "email must not exceed 100 characters")
        String email,
        @NotBlank(message = "phoneNumber is required")
        @Pattern(regexp = "^\\+?[0-9 \\-]{8,20}$", message = "phoneNumber is not valid")
        String phoneNumber,
        @Size(max = 100, message = "a business can register at most 100 products at once")
        List<@Valid ProductRequest> products
) {
}
