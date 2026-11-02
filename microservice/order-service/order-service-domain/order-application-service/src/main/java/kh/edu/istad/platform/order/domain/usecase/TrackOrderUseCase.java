package kh.edu.istad.platform.order.domain.usecase;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.common.domain.valueobject.TrackingId;
import kh.edu.istad.platform.order.domain.dto.TrackOrderResult;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrackOrderUseCase {

    private final OrderRepository orderRepository;

    public Mono<TrackOrderResult> execute(UUID trackingId) {
        log.info("Tracking order with trackingId: {}", trackingId);

        return orderRepository.findByTrackingId(new TrackingId(trackingId))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Not found: " + trackingId)))
                .map(order -> new TrackOrderResult(
                        order.getId().value(),
                        order.getTrackingId().value(),
                        order.getCustomerId().value(),
                        order.getOrderStatus(),
                        order.getPrice().amount(),
                        order.getFailureMessages()
                ));
    }
}