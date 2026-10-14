package kh.edu.istad.platform.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.exception.DomainException;
import kh.edu.istad.common.domain.valueobject.CreditEntryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.platform.domain.exception.InsufficientCreditException;

import java.util.UUID;

public class CreditEntry extends BaseEntity<CreditEntryId> {

    private final CustomerId customerId;
    private Money totalCreditAmount;

    public CreditEntry(CreditEntryId id, CustomerId customerId, Money totalCreditAmount) {
        setId(id);
        this.customerId = customerId;
        this.totalCreditAmount = totalCreditAmount;
    }

    public static CreditEntry create(CustomerId customerId) {
        return new CreditEntry(new CreditEntryId(UUID.randomUUID()), customerId, Money.ZERO);
    }

    public void addCreditAmount(Money amount) {
        this.totalCreditAmount = totalCreditAmount.add(amount);
    }

    public void subtractCreditAmount(Money amount) {
        if (totalCreditAmount.isLessThan(amount)) {
            throw new InsufficientCreditException(
                    "Insufficient credit: available balance " + totalCreditAmount.amount() + " is less than requested amount " + amount.amount());
        }
        this.totalCreditAmount = totalCreditAmount.subtract(amount);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }
}
