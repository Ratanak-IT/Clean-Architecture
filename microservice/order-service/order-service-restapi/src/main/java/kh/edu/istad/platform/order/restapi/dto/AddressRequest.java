package kh.edu.istad.platform.order.restapi.dto;

import kh.edu.istad.common.domain.valueobject.StreetAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "street is required")
        @Size(max = 50, message = "street is too long")
        String street,
        @NotBlank(message = "postalCode is required")
        @Size(max = 10, message = "postalCode is too long")
        String postalCode,
        @NotBlank(message = "city is required")
        @Size(max = 50, message = "city is too long")
        String city
) {
}
