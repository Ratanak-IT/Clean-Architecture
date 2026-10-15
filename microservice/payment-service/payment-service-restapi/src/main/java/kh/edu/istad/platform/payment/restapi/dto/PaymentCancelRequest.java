package kh.edu.istad.platform.payment.restapi.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentCancelRequest(
        UUID paymentId,
        UUID orderId,
        UUID customerId,
        BigDecimal price
) {
}
