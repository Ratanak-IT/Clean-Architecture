package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.domain.mapper.PaymentMapper;
import kh.edu.istad.platform.payment.domain.port.out.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetPaymentByOrderIdUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResult execute(UUID orderId) {
        log.info("Executing get payment by order ID usecase for order ID: {}", orderId);
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentDomainException("Payment for order ID: " + orderId + " not found"));
        return paymentMapper.toResult(payment, Collections.emptyList());
    }
}
