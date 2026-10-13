package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.common.domain.valueobject.TransactionType;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.domain.event.PaymentCompletedEvent;
import kh.edu.istad.platform.domain.event.PaymentEvent;
import kh.edu.istad.platform.domain.event.PaymentFailedEvent;
import kh.edu.istad.platform.domain.service.PaymentDomainService;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.domain.dto.ProcessPaymentCommand;
import kh.edu.istad.platform.payment.domain.mapper.PaymentMapper;
import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.domain.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.domain.port.out.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentDomainService paymentDomainService;
    private final PaymentMapper paymentMapper;

    public PaymentResult execute(ProcessPaymentCommand command) {
        log.info("Processing payment usecase with command: {}", command);

        Payment payment = paymentMapper.toPayment(command);
        List<String> failureMessages = new ArrayList<>();

        CreditEntry creditEntry = creditEntryRepository.findByCustomerId(command.customerId())
                .orElse(null);

        if (creditEntry == null) {
            log.error("Credit entry not found for customer ID: {}", command.customerId());
            failureMessages.add("Credit entry not found for customer ID: " + command.customerId());
            payment.initializePayment();
            payment.updateStatus(PaymentStatus.FAILED);
            Payment savedPayment = paymentRepository.save(payment);
            PaymentFailedEvent failedEvent = new PaymentFailedEvent(savedPayment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
            log.warn("Payment failed event created for order ID: {} at {}", command.orderId(), failedEvent.getCreatedAt());
            return paymentMapper.toResult(savedPayment, failureMessages);
        }

        List<CreditHistory> creditHistories = creditHistoryRepository.findByCustomerId(command.customerId());

        PaymentEvent paymentEvent = paymentDomainService.processPayment(payment, creditEntry, creditHistories, failureMessages);

        if (paymentEvent instanceof PaymentCompletedEvent completedEvent) {
            creditEntryRepository.save(creditEntry);
            CreditHistory creditHistory = CreditHistory.of(
                    payment.getCustomerId(),
                    payment.getPrice(),
                    TransactionType.DEBIT
            );
            creditHistoryRepository.save(creditHistory);
            log.info("Payment completed successfully with ID: {} for order ID: {} at {}",
                    payment.getId().value(), payment.getOrderId().value(), completedEvent.getCreatedAt());
        } else if (paymentEvent instanceof PaymentFailedEvent failedEvent) {
            log.warn("Payment failed for order ID: {}. Reasons: {} at {}",
                    command.orderId(), failureMessages, failedEvent.getCreatedAt());
        }

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResult(savedPayment, failureMessages);
    }
}
