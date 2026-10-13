package kh.edu.istad.platform.order.domain.port.out;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.order.domain.dto.PaymentResponseDto;

public interface PaymentRepository {
    PaymentResponseDto processPayment(OrderId orderId, CustomerId customerId, Money price);
}
