package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerSegmentRequest(
        @NotBlank String code,
        @NotBlank String name
) {
}