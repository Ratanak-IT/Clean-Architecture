package kh.edu.istad.platform.business.restapi.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must not exceed 100 characters")
        String name,
        @NotNull(message = "price is required")
        @Positive(message = "price must be greater than zero")
        @Digits(integer = 8, fraction = 2, message = "price must have at most 2 decimal places")
        BigDecimal price,
        @NotNull(message = "quantity is required")
        @PositiveOrZero(message = "quantity must not be negative")
        Integer quantity
) {
}
