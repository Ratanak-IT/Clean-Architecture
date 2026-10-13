package kh.edu.istad.platform.order.restapi.dto;

import kh.edu.istad.common.domain.valueobject.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record TrackOrderResponse(
        UUID orderId,
        UUID trackingId,
        UUID customerId,
        OrderStatus orderStatus,
        BigDecimal price,
        List<String> failureMessages
) {
}
