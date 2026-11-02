package kh.edu.istad.platform.order.domain.port.out;

import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.TrackingId;
import kh.edu.istad.platform.order.domain.entity.Order;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;


public interface OrderRepository {
    Mono<Order> save(Order order);
    Mono<Order> findById(OrderId orderId);
    Mono<Order> findByTrackingId(TrackingId trackingId);
}

