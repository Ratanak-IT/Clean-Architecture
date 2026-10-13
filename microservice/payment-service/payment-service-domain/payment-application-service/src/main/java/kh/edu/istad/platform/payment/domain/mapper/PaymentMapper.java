package kh.edu.istad.platform.payment.domain.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.domain.dto.ProcessPaymentCommand;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PaymentMapper {

    public Payment toPayment(ProcessPaymentCommand command) {
        if (command == null) return null;

        return new Payment(
                new OrderId(command.orderId()),
                new CustomerId(command.customerId()),
                new Money(command.price())
        );
    }

    public PaymentResult toResult(Payment payment, List<String> failureMessages) {
        if (payment == null) return null;

        return new PaymentResult(
                payment.getId() != null ? payment.getId().value() : null,
                payment.getCustomerId() != null ? payment.getCustomerId().value() : null,
                payment.getOrderId() != null ? payment.getOrderId().value() : null,
                payment.getPrice() != null ? payment.getPrice().amount() : null,
                payment.getPaymentStatus(),
                payment.getCreatedAt(),
                failureMessages != null ? failureMessages : Collections.emptyList()
        );
    }
}
