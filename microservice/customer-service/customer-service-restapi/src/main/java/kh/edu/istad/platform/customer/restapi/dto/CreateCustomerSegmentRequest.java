package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerSegmentRequest(
        @NotBlank String code,
        @NotBlank String name
) {
}
