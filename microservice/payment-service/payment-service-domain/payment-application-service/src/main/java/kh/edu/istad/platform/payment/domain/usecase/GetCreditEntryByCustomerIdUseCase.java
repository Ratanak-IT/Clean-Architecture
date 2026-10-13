package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.domain.dto.CreditEntryResult;
import kh.edu.istad.platform.payment.domain.mapper.CreditEntryMapper;
import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetCreditEntryByCustomerIdUseCase {

    private final CreditEntryRepository creditEntryRepository;
    private final CreditEntryMapper creditEntryMapper;

    public CreditEntryResult execute(UUID customerId) {
        log.info("Executing get credit entry for customer ID: {}", customerId);
        CreditEntry creditEntry = creditEntryRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new PaymentDomainException("Credit entry for customer ID: " + customerId + " not found"));
        return creditEntryMapper.toResult(creditEntry);
    }
}
