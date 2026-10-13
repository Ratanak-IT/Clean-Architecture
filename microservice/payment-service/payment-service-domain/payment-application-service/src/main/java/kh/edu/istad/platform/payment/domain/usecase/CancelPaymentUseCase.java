package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.common.domain.valueobject.TransactionType;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.domain.event.PaymentCancelledEvent;
import kh.edu.istad.platform.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.domain.service.PaymentDomainService;
import kh.edu.istad.platform.payment.domain.dto.CancelPaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.domain.mapper.PaymentMapper;
import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.domain.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.domain.port.out.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CancelPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentDomainService paymentDomainService;
    private final PaymentMapper paymentMapper;

    public PaymentResult execute(CancelPaymentCommand command) {
        log.info("Executing cancel payment usecase for command: {}", command);

        Payment payment = findPayment(command);
        if (payment == null) {
            log.error("Payment not found for cancel command: {}", command);
            throw new PaymentDomainException("Payment not found for order ID: " + command.orderId());
        }

        UUID customerId = payment.getCustomerId().value();
        CreditEntry creditEntry = creditEntryRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new PaymentDomainException("Credit entry not found for customer ID: " + customerId));

        PaymentCancelledEvent cancelledEvent = paymentDomainService.cancelPayment(payment, creditEntry);

        creditEntryRepository.save(creditEntry);

        CreditHistory refundHistory = CreditHistory.of(
                payment.getCustomerId(),
                payment.getPrice(),
                TransactionType.CREDIT
        );
        creditHistoryRepository.save(refundHistory);

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment cancelled successfully with ID: {} for order ID: {} at {}",
                savedPayment.getId().value(), savedPayment.getOrderId().value(), cancelledEvent.getCreatedAt());

        return paymentMapper.toResult(savedPayment, Collections.emptyList());
    }

    private Payment findPayment(CancelPaymentCommand command) {
        if (command.paymentId() != null) {
            var byId = paymentRepository.findById(command.paymentId());
            if (byId.isPresent()) {
                return byId.get();
            }
        }
        if (command.orderId() != null) {
            var byOrderId = paymentRepository.findByOrderId(command.orderId());
            if (byOrderId.isPresent()) {
                return byOrderId.get();
            }
        }
        return null;
    }
}
