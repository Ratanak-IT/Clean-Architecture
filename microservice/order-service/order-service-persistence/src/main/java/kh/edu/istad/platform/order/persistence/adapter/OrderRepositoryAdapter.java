package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.TrackingId;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.mapper.OrderPersistenceMapper;
import kh.edu.istad.platform.order.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public Order save(Order order) {
        if (order.getId() != null && order.getId().value() != null) {
            Optional<OrderEntity> existingOpt = orderJpaRepository.findById(order.getId().value());
            if (existingOpt.isPresent()) {
                OrderEntity existing = existingOpt.get();
                existing.setOrderStatus(order.getOrderStatus());
                existing.setFailureMessages(
                        order.getFailureMessages() != null
                                ? new ArrayList<>(order.getFailureMessages())
                                : new ArrayList<>()
                );
                return mapper.toDomain(orderJpaRepository.save(existing));
            }
        }
        return mapper.toDomain(orderJpaRepository.save(mapper.toEntity(order)));
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJpaRepository.findById(orderId.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.value()).map(mapper::toDomain);
    }
}

