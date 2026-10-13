package kh.edu.istad.platform.business.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResult(UUID productId, String name, BigDecimal price, int quantity, boolean available) {
}
