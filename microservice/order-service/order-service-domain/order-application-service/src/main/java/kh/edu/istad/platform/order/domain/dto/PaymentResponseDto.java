package kh.edu.istad.platform.order.domain.dto;

import kh.edu.istad.common.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PaymentResponseDto(
        UUID paymentId,
        UUID customerId,
        UUID orderId,
        BigDecimal price,
        PaymentStatus paymentStatus,
        List<String> failureMessages
) {
}
