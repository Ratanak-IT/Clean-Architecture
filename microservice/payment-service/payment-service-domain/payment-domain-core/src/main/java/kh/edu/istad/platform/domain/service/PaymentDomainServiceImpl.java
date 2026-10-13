package kh.edu.istad.platform.domain.service;

import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.common.domain.valueobject.TransactionType;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.domain.event.PaymentCancelledEvent;
import kh.edu.istad.platform.domain.event.PaymentCompletedEvent;
import kh.edu.istad.platform.domain.event.PaymentEvent;
import kh.edu.istad.platform.domain.event.PaymentFailedEvent;
import kh.edu.istad.platform.domain.exception.PaymentDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class PaymentDomainServiceImpl implements PaymentDomainService {

    @Override
    public PaymentEvent processPayment(Payment payment, CreditEntry entry, List<CreditHistory> histories, List<String> failures) {
        payment.validatePayment(failures);
        validateCredit(payment, entry, failures);
        validateHistory(entry, histories, failures);
        payment.initializePayment();

        if (failures.isEmpty()) {
            entry.subtractCreditAmount(payment.getPrice());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failures);
        } else {
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failures);
        }
    }

    @Override
    public PaymentCancelledEvent cancelPayment(Payment payment, CreditEntry entry) {
        if (payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new PaymentDomainException("Only completed payments can be cancelled");
        }
        entry.addCreditAmount(payment.getPrice());
        payment.updateStatus(PaymentStatus.CANCELLED);
        return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), Collections.emptyList());
    }

    @Override
    public void validateCredit(Payment payment, CreditEntry entry, List<String> failures) {
        if (entry == null) {
            failures.add("Credit entry is missing");
            return;
        }
        if (entry.getTotalCreditAmount().isLessThan(payment.getPrice())) {
            failures.add("Insufficient credit. Required: " + payment.getPrice().amount()
                    + ", available: " + entry.getTotalCreditAmount().amount());
        }
    }

    @Override
    public void validateHistory(CreditEntry entry, List<CreditHistory> histories, List<String> failures) {
        if (entry == null) {
            return;
        }
        if (histories == null) {
            failures.add("Credit history is missing");
            return;
        }
        Money credits = sum(histories, TransactionType.CREDIT);
        Money debits = sum(histories, TransactionType.DEBIT);
        if (!credits.subtract(debits).equals(entry.getTotalCreditAmount())) {
            failures.add("Credit history does not match the total credit amount");
        }
    }

    @Override
    public Money sum(List<CreditHistory> histories, TransactionType type) {
        if (histories == null) {
            return Money.ZERO;
        }
        return histories.stream()
                .filter(h -> h.getTransactionType() == type)
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }
}
