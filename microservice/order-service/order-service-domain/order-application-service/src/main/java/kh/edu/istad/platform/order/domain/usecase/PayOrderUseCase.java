package kh.edu.istad.platform.order.domain.usecase;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.OrderStatus;
import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.platform.order.domain.dto.PayOrderResult;
import kh.edu.istad.platform.order.domain.dto.PaymentResponseDto;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.event.OrderPaidEvent;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.port.out.PaymentRepository;
import kh.edu.istad.platform.order.domain.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class PayOrderUseCase {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderDomainService orderDomainService;

    public PayOrderResult execute(UUID orderId) {
        log.info("Executing pay order usecase for orderId: {}", orderId);
        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if (order.getOrderStatus() == OrderStatus.PAID) {
            log.warn("Order {} is already PAID", orderId);
            return new PayOrderResult(
                    order.getId().value(),
                    order.getTrackingId().value(),
                    order.getOrderStatus(),
                    "Order is already paid"
            );
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new OrderDomainException(
                    "Cannot pay order with ID: " + orderId + " because current status is " + order.getOrderStatus() + " (expected PENDING)");
        }

        // call payment-service via Restapi
        PaymentResponseDto paymentResponse = paymentRepository.processPayment(
                order.getId(),
                order.getCustomerId(),
                order.getPrice()
        );

        if (paymentResponse != null && PaymentStatus.COMPLETED.equals(paymentResponse.paymentStatus())) {
            OrderPaidEvent paidEvent = orderDomainService.payOrder(order);
            Order savedOrder = orderRepository.save(order);
            log.info("Order {} successfully paid! Status: {}", savedOrder.getId().value(), savedOrder.getOrderStatus());
            return new PayOrderResult(
                    savedOrder.getId().value(),
                    savedOrder.getTrackingId().value(),
                    savedOrder.getOrderStatus(),
                    "Order paid successfully"
            );
        } else {
            List<String> failureMessages = (paymentResponse != null && paymentResponse.failureMessages() != null && !paymentResponse.failureMessages().isEmpty())
                    ? paymentResponse.failureMessages()
                    : List.of("Payment failed");

            orderDomainService.cancelOrder(order, failureMessages);
            Order savedOrder = orderRepository.save(order);
            log.warn("Payment failed for order {}. Status: {}. Reasons: {}",
                    savedOrder.getId().value(), savedOrder.getOrderStatus(), failureMessages);
            return new PayOrderResult(
                    savedOrder.getId().value(),
                    savedOrder.getTrackingId().value(),
                    savedOrder.getOrderStatus(),
                    "Payment failed: " + String.join(", ", failureMessages)
            );
        }
    }
}
