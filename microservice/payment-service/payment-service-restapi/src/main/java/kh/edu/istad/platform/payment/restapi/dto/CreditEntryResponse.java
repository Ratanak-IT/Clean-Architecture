package kh.edu.istad.platform.payment.restapi.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreditEntryResponse(
        UUID creditEntryId,
        UUID customerId,
        BigDecimal totalCreditAmount
) {
}
