package kh.edu.istad.platform.order.persistence.repository;

import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderReactiveRepository extends R2dbcRepository<OrderEntity, UUID> {
    Mono<OrderEntity> findByTrackingId(UUID trackingId);
}

