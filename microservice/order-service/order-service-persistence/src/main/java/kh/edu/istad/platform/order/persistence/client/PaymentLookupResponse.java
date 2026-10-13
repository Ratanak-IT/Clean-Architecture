package kh.edu.istad.platform.order.persistence.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kh.edu.istad.common.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentLookupResponse(
        UUID paymentId,
        UUID customerId,
        UUID orderId,
        BigDecimal price,
        PaymentStatus paymentStatus,
        ZonedDateTime createdAt,
        List<String> failureMessages
) {
}
