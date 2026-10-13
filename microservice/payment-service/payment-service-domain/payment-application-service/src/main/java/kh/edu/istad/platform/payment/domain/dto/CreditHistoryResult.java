package kh.edu.istad.platform.payment.domain.dto;

import kh.edu.istad.common.domain.valueobject.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record CreditHistoryResult(
        UUID creditHistoryId,
        UUID customerId,
        BigDecimal amount,
        TransactionType transactionType
) {
}
