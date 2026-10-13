package kh.edu.istad.platform.order.domain.dto;

import kh.edu.istad.common.domain.valueobject.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record TrackOrderResult(
        UUID orderId,
        UUID trackingId,
        UUID customerId,
        OrderStatus orderStatus,
        BigDecimal price,
        List<String> failureMessages
) {
}
