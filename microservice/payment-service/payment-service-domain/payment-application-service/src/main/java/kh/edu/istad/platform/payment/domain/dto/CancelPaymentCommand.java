package kh.edu.istad.platform.payment.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CancelPaymentCommand(
        UUID paymentId,
        UUID orderId,
        UUID customerId,
        BigDecimal price
) {
}
