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
public class GetPaymentByIdUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResult execute(UUID paymentId) {
        log.info("Executing get payment by ID usecase for ID: {}", paymentId);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentDomainException("Payment with ID: " + paymentId + " not found"));
        return paymentMapper.toResult(payment, Collections.emptyList());
    }
}
