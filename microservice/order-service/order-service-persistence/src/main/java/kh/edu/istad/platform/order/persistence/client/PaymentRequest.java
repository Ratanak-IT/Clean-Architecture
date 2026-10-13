package kh.edu.istad.platform.order.persistence.client;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        UUID orderId,
        UUID customerId,
        BigDecimal price
) {
}
