package kh.edu.istad.platform.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.CreditHistoryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.TransactionType;

import java.util.UUID;

public class CreditHistory extends BaseEntity<CreditHistoryId> {
    private final CustomerId customerId;
    private final Money amount;
    private final TransactionType transactionType;

    public CreditHistory(CreditHistoryId id, CustomerId customerId, Money amount, TransactionType type) {
        setId(id);
        this.customerId = customerId;
        this.amount = amount;
        this.transactionType = type;
    }

    public static CreditHistory of(CustomerId customerId, Money amount, TransactionType type) {
        return new CreditHistory(new CreditHistoryId(UUID.randomUUID()), customerId, amount, type);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
