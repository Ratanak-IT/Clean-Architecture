package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CreditEntryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.TransactionType;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.dto.CreditEntryResult;
import kh.edu.istad.platform.payment.domain.dto.TopUpCreditCommand;
import kh.edu.istad.platform.payment.domain.mapper.CreditEntryMapper;
import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.domain.port.out.CreditHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class TopUpCreditUseCase {

    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final CreditEntryMapper creditEntryMapper;

    public CreditEntryResult execute(TopUpCreditCommand command) {
        log.info("Executing top up credit use case for customer ID: {} with amount: {}",
                command.customerId(), command.amount());

        Money topUpAmount = new Money(command.amount());
        CustomerId customerId = new CustomerId(command.customerId());

        CreditEntry creditEntry = creditEntryRepository.findByCustomerId(command.customerId())
                .orElse(null);

        if (creditEntry == null) {
            log.info("No credit entry found for customer ID: {}. Initializing new credit entry.", command.customerId());
            creditEntry = new CreditEntry(new CreditEntryId(UUID.randomUUID()), customerId, topUpAmount);
        } else {
            log.info("Existing credit entry found. Current balance: {}, adding: {}",
                    creditEntry.getTotalCreditAmount().amount(), topUpAmount.amount());
            creditEntry.addCreditAmount(topUpAmount);
        }

        CreditEntry savedEntry = creditEntryRepository.save(creditEntry);

        CreditHistory history = CreditHistory.of(customerId, topUpAmount, TransactionType.CREDIT);
        creditHistoryRepository.save(history);

        log.info("Credit successfully topped up for customer ID: {}. New total: {}",
                command.customerId(), savedEntry.getTotalCreditAmount().amount());

        return creditEntryMapper.toResult(savedEntry);
    }
}
