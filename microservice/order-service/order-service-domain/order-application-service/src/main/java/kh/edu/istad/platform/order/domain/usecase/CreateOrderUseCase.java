package kh.edu.istad.platform.order.domain.usecase;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.dto.PaymentResponseDto;
import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.domain.event.OrderPaidEvent;
import kh.edu.istad.platform.order.domain.mapper.CreateOrderMapper;
import kh.edu.istad.platform.order.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.port.out.PaymentRepository;
import kh.edu.istad.platform.order.domain.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderDomainService orderDomainService;
    private final CreateOrderMapper mapper;

    public Mono<CreateOrderResult> execute(CreateOrderCommand command) {
        log.info("Creating order for customer {} with business {}", command.customerId(), command.businessId());

        Mono<CustomerId> customerMono = customerRepository.findById(new CustomerId(command.customerId()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer", command.customerId())))
                .map(c -> new CustomerId(command.customerId()));

        Mono<Business> businessMono = Mono.justOrEmpty(businessRepository.findById(new BusinessId(command.businessId())))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Business", command.businessId())));

        return Mono.zip(customerMono, businessMono)
                .flatMap(tuple -> {
                    Business business = tuple.getT2();

                    Order order = mapper.toOrder(command);
                    OrderCreatedEvent event = orderDomainService.validateAndInitiateOrder(order, business);

                    return orderRepository.save(order)
                            .flatMap(saved -> {
                                log.info("Order {} created with tracking id {} (status: {})",
                                        saved.getId().value(), saved.getTrackingId().value(), saved.getOrderStatus());

                                PaymentResponseDto paymentResponse = paymentRepository.processPayment(
                                        saved.getId(),
                                        saved.getCustomerId(),
                                        saved.getPrice()
                                );

                                if (paymentResponse != null && PaymentStatus.COMPLETED.equals(paymentResponse.paymentStatus())) {
                                    OrderPaidEvent paidEvent = orderDomainService.payOrder(saved);
                                    return orderRepository.save(saved)
                                            .map(paidOrder -> {
                                                log.info("Payment succeeded for order {}. New OrderStatus: {}",
                                                        paidOrder.getId().value(), paidOrder.getOrderStatus());
                                                return mapper.toResult(paidOrder, "Order created and paid successfully");
                                            });
                                } else {
                                    List<String> failureMessages = (paymentResponse != null && paymentResponse.failureMessages() != null && !paymentResponse.failureMessages().isEmpty())
                                            ? paymentResponse.failureMessages()
                                            : List.of("Payment failed");

                                    return orderRepository.save(saved)
                                            .map(cancelledOrder -> {
                                                log.warn("Payment failed for order {}. New OrderStatus: {}. Reasons: {}",
                                                        cancelledOrder.getId().value(), cancelledOrder.getOrderStatus(), failureMessages);
                                                return mapper.toResult(cancelledOrder, "Order created but payment failed: " + String.join(", ", failureMessages));
                                            });
                                }
                            });
                });
    }
}