package kh.edu.istad.platform.payment.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreditEntryResult(
        UUID creditEntryId,
        UUID customerId,
        BigDecimal totalCreditAmount
) {
}
