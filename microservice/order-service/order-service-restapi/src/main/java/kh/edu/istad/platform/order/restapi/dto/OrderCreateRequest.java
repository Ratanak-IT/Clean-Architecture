package kh.edu.istad.platform.order.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderCreateRequest(
        @NotNull(message = "customerId is required")
        UUID customerId,
        @NotNull(message = "businessId is required")
        UUID businessId,
        @NotNull(message = "price is required")
        @Positive(message = "price must be greater than zero")
        @Digits(integer = 8, fraction = 2, message = "price must have at most 2 decimal places")
        BigDecimal price,
        @NotEmpty(message = "items must not be empty")
        List<@Valid @NotNull OrderItemRequest> items,
        @NotNull(message = "address is required")
        @Valid
        AddressRequest address
) {
}
