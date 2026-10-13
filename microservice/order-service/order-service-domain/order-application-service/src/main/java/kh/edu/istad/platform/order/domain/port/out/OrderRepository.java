package kh.edu.istad.platform.order.domain.port.out;

import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.TrackingId;
import kh.edu.istad.platform.order.domain.entity.Order;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(OrderId orderId);
    Optional<Order> findByTrackingId(TrackingId trackingId);
}

