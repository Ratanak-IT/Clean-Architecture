package kh.edu.istad.platform.domain.service;

import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.TransactionType;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.domain.event.PaymentCancelledEvent;
import kh.edu.istad.platform.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {
    PaymentEvent processPayment(Payment payment, CreditEntry entry, List<CreditHistory> histories, List<String> failures);
    PaymentCancelledEvent cancelPayment(Payment payment, CreditEntry entry);
    void validateCredit(Payment payment, CreditEntry entry, List<String> failures);
    void validateHistory(CreditEntry entry, List<CreditHistory> histories, List<String> failures);
    Money sum(List<CreditHistory> histories, TransactionType type);
}
