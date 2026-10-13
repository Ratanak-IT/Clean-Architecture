package kh.edu.istad.platform.business.restapi.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(UUID productId, String name, BigDecimal price, int quantity, boolean available) {
}
