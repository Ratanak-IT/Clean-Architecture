package kh.edu.istad.platform.payment.domain.dto;

import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.platform.domain.entity.Payment;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record PaymentResult(
        UUID paymentId,
        UUID customerId,
        UUID orderId,
        BigDecimal price,
        PaymentStatus paymentStatus,
        ZonedDateTime createdAt,
        List<String> failureMessages
) {
    public PaymentResult(Payment payment, List<String> failureMessages) {
        this(
                payment != null && payment.getId() != null ? payment.getId().value() : null,
                payment != null && payment.getCustomerId() != null ? payment.getCustomerId().value() : null,
                payment != null && payment.getOrderId() != null ? payment.getOrderId().value() : null,
                payment != null && payment.getPrice() != null ? payment.getPrice().amount() : null,
                payment != null ? payment.getPaymentStatus() : null,
                payment != null ? payment.getCreatedAt() : null,
                failureMessages != null ? failureMessages : Collections.emptyList()
        );
    }

    public boolean isSuccess() {
        return paymentStatus == PaymentStatus.COMPLETED && (failureMessages == null || failureMessages.isEmpty());
    }
}
