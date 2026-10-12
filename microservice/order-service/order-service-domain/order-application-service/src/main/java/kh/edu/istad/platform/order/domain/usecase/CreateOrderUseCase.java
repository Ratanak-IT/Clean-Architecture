package kh.edu.istad.platform.order.domain.usecase;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.domain.mapper.CreateOrderMapper;
import kh.edu.istad.platform.order.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;
    private final CreateOrderMapper mapper;


    public CreateOrderResult execute(CreateOrderCommand command) {
        log.info("Creating order for customer {} with business {}", command.customerId(), command.businessId());

        customerRepository.findById(new CustomerId(command.customerId()))
                .orElseThrow(() -> new ResourceNotFoundException("Customer", command.customerId()));
        Business business = businessRepository.findById(new BusinessId(command.businessId()))
                .orElseThrow(() -> new ResourceNotFoundException("Business", command.businessId()));

        Order order = mapper.toOrder(command);
        OrderCreatedEvent event = orderDomainService.validateAndInitiateOrder(order, business);
//        order.initializeOrder();
        Order saved = orderRepository.save(order);

        log.info("Order {} created with tracking id {} ({} at {})", saved.getId().value(),
                saved.getTrackingId().value(), event.getClass().getSimpleName(), event.getCreatedAt());
        return mapper.toResult(saved, "Order created successfully");
    }
}
