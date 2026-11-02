package kh.edu.istad.platform.order.persistence.repository;

import kh.edu.istad.platform.order.persistence.entity.OrderAddressEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderAddressR2dbcRepository extends ReactiveCrudRepository<OrderAddressEntity, UUID> {
    @Query("SELECT oa.* FROM order_addresses oa JOIN orders o ON o.order_address_id = oa.id WHERE o.id = :orderId")
    Mono<OrderAddressEntity> findByOrderId(UUID orderId);
}