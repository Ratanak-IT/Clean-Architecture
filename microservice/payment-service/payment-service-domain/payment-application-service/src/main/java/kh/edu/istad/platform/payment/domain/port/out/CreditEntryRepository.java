package kh.edu.istad.platform.payment.domain.port.out;

import kh.edu.istad.platform.domain.entity.CreditEntry;

import java.util.Optional;
import java.util.UUID;

public interface CreditEntryRepository {
    CreditEntry save(CreditEntry creditEntry);
    Optional<CreditEntry> findByCustomerId(UUID customerId);
}
