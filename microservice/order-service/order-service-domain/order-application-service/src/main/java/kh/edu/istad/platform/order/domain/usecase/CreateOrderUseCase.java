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

    public CreateOrderResult execute(CreateOrderCommand command) {
        log.info("Creating order for customer {} with business {}", command.customerId(), command.businessId());

        customerRepository.findById(new CustomerId(command.customerId()))
                .orElseThrow(() -> new ResourceNotFoundException("Customer", command.customerId()));
        Business business = businessRepository.findById(new BusinessId(command.businessId()))
                .orElseThrow(() -> new ResourceNotFoundException("Business", command.businessId()));

        Order order = mapper.toOrder(command);
        OrderCreatedEvent event = orderDomainService.validateAndInitiateOrder(order, business);
        Order saved = orderRepository.save(order);

        log.info("Order {} created with tracking id {} (status: {})",
                saved.getId().value(), saved.getTrackingId().value(), saved.getOrderStatus());

        // Process payment synchronously with payment-service via REST API
        PaymentResponseDto paymentResponse = paymentRepository.processPayment(
                saved.getId(),
                saved.getCustomerId(),
                saved.getPrice()
        );

        if (paymentResponse != null && PaymentStatus.COMPLETED.equals(paymentResponse.paymentStatus())) {
            OrderPaidEvent paidEvent = orderDomainService.payOrder(saved);
            Order paidOrder = orderRepository.save(saved);
            log.info("Payment succeeded for order {}. New OrderStatus: {}",
                    paidOrder.getId().value(), paidOrder.getOrderStatus());
            return mapper.toResult(paidOrder, "Order created and paid successfully");
        } else {
            List<String> failureMessages = (paymentResponse != null && paymentResponse.failureMessages() != null && !paymentResponse.failureMessages().isEmpty())
                    ? paymentResponse.failureMessages()
                    : List.of("Payment failed");

            orderDomainService.cancelOrder(saved, failureMessages);
            Order cancelledOrder = orderRepository.save(saved);
            log.warn("Payment failed for order {}. New OrderStatus: {}. Reasons: {}",
                    cancelledOrder.getId().value(), cancelledOrder.getOrderStatus(), failureMessages);
            return mapper.toResult(cancelledOrder, "Order created but payment failed: " + String.join(", ", failureMessages));
        }
    }
}

