package kh.edu.istad.platform.payment.domain.mapper;

import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.dto.CreditEntryResult;
import kh.edu.istad.platform.payment.domain.dto.CreditHistoryResult;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryMapper {

    public CreditEntryResult toResult(CreditEntry creditEntry) {
        if (creditEntry == null) return null;

        return new CreditEntryResult(
                creditEntry.getId() != null ? creditEntry.getId().value() : null,
                creditEntry.getCustomerId() != null ? creditEntry.getCustomerId().value() : null,
                creditEntry.getTotalCreditAmount() != null ? creditEntry.getTotalCreditAmount().amount() : null
        );
    }

    public CreditHistoryResult toResult(CreditHistory creditHistory) {
        if (creditHistory == null) return null;

        return new CreditHistoryResult(
                creditHistory.getId() != null ? creditHistory.getId().value() : null,
                creditHistory.getCustomerId() != null ? creditHistory.getCustomerId().value() : null,
                creditHistory.getAmount() != null ? creditHistory.getAmount().amount() : null,
                creditHistory.getTransactionType()
        );
    }
}
