package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.TrackingId;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.persistence.entity.OrderAddressEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.mapper.OrderPersistenceMapper;
import kh.edu.istad.platform.order.persistence.repository.OrderAddressR2dbcRepository;
import kh.edu.istad.platform.order.persistence.repository.OrderItemR2dbcRepository;
import kh.edu.istad.platform.order.persistence.repository.OrderReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderReactiveRepository orderRepository;
    private final OrderAddressR2dbcRepository addressRepository;
    private final OrderItemR2dbcRepository itemRepository;
    private final R2dbcEntityTemplate template;
    private final TransactionalOperator tx;
    private final OrderPersistenceMapper mapper;

    @Override
    public Mono<Order> save(Order order) {
        return upsert(order)
                .flatMap(this::load)
                .as(tx::transactional);
    }

    @Override
    public Mono<Order> findById(OrderId orderId) {
        return orderRepository.findById(orderId.value())
                .flatMap(this::load);
    }

    @Override
    public Mono<Order> findByTrackingId(TrackingId trackingId) {
        return orderRepository.findByTrackingId(trackingId.value())
                .flatMap(this::load);
    }

    private Mono<OrderEntity> upsert(Order order) {
        return orderRepository.findById(order.getId().value())
                .flatMap(existing -> {
                    OrderEntity mapped = mapper.toEntity(order);
                    existing.setOrderStatus(mapped.getOrderStatus());
                    existing.setFailureMessages(mapped.getFailureMessages());
                    return orderRepository.save(existing);
                })
                .switchIfEmpty(Mono.defer(() -> insert(order)));
    }

    private Mono<OrderEntity> insert(Order order) {
        return template.insert(mapper.toAddressEntity(order))
                .then(template.insert(mapper.toEntity(order)))
                .flatMap(saved -> Flux.fromIterable(mapper.toItemEntities(order))
                        .concatMap(template::insert)
                        .then(Mono.just(saved)));
    }

    private Mono<Order> load(OrderEntity entity) {
        Mono<OrderAddressEntity> addressMono = entity.getOrderAddressId() != null
                ? addressRepository.findById(entity.getOrderAddressId())
                : addressRepository.findByOrderId(entity.getId());

        return Mono.zip(
                addressMono,
                itemRepository.findByOrderId(entity.getId()).collectList()
        ).map(t -> mapper.toDomain(entity, t.getT1(), t.getT2()));
    }
}