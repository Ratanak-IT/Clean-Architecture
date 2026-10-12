package kh.edu.istad.platform.order.restapi.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequest(
        @NotNull(message = "productId is required")
        UUID productId,
        @NotNull(message = "quantity is required")
        @Positive(message = "quantity must be at least 1")
        Integer quantity,
        @NotNull(message = "price is required")
        @Positive(message = "price must be greater than zero")
        @Digits(integer = 8, fraction = 2, message = "price must have at most 2 decimal places")
        BigDecimal price,
        @NotNull(message = "subTotal is required")
        @Positive(message = "subTotal must be greater than zero")
        @Digits(integer = 8, fraction = 2, message = "subTotal must have at most 2 decimal places")
        BigDecimal subTotal
) {
}
