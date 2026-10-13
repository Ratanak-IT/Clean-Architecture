package kh.edu.istad.platform.business.domain.dto;

import java.math.BigDecimal;

public record ProductCommand(String name, BigDecimal price, int quantity) {
}
