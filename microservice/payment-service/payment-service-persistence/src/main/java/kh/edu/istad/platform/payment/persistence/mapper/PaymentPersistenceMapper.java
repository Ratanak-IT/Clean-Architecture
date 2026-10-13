package kh.edu.istad.platform.payment.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.CreditEntryId;
import kh.edu.istad.common.domain.valueobject.CreditHistoryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.PaymentId;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.payment.persistence.entity.CreditEntryEntity;
import kh.edu.istad.platform.payment.persistence.entity.CreditHistoryEntity;
import kh.edu.istad.platform.payment.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceMapper {

    public PaymentEntity toEntity(Payment payment) {
        if (payment == null) return null;
        return new PaymentEntity(
                payment.getId() != null ? payment.getId().value() : null,
                payment.getCustomerId() != null ? payment.getCustomerId().value() : null,
                payment.getOrderId() != null ? payment.getOrderId().value() : null,
                payment.getPrice() != null ? payment.getPrice().amount() : null,
                payment.getPaymentStatus(),
                payment.getCreatedAt()
        );
    }

    public Payment toDomain(PaymentEntity entity) {
        if (entity == null) return null;
        return new Payment(
                entity.getId() != null ? new PaymentId(entity.getId()) : null,
                entity.getOrderId() != null ? new OrderId(entity.getOrderId()) : null,
                entity.getCustomerId() != null ? new CustomerId(entity.getCustomerId()) : null,
                entity.getPrice() != null ? new Money(entity.getPrice()) : null,
                entity.getPaymentStatus(),
                entity.getCreatedAt()
        );
    }

    public CreditEntryEntity toEntity(CreditEntry entry) {
        if (entry == null) return null;
        return new CreditEntryEntity(
                entry.getId() != null ? entry.getId().value() : null,
                entry.getCustomerId() != null ? entry.getCustomerId().value() : null,
                entry.getTotalCreditAmount() != null ? entry.getTotalCreditAmount().amount() : null
        );
    }

    public CreditEntry toDomain(CreditEntryEntity entity) {
        if (entity == null) return null;
        return new CreditEntry(
                entity.getId() != null ? new CreditEntryId(entity.getId()) : null,
                entity.getCustomerId() != null ? new CustomerId(entity.getCustomerId()) : null,
                entity.getTotalCreditAmount() != null ? new Money(entity.getTotalCreditAmount()) : null
        );
    }

    public CreditHistoryEntity toEntity(CreditHistory history) {
        if (history == null) return null;
        return new CreditHistoryEntity(
                history.getId() != null ? history.getId().value() : null,
                history.getCustomerId() != null ? history.getCustomerId().value() : null,
                history.getAmount() != null ? history.getAmount().amount() : null,
                history.getTransactionType()
        );
    }

    public CreditHistory toDomain(CreditHistoryEntity entity) {
        if (entity == null) return null;
        return new CreditHistory(
                entity.getId() != null ? new CreditHistoryId(entity.getId()) : null,
                entity.getCustomerId() != null ? new CustomerId(entity.getCustomerId()) : null,
                entity.getAmount() != null ? new Money(entity.getAmount()) : null,
                entity.getTransactionType()
        );
    }
}
