package kh.edu.istad.platform.payment.domain.port.out;

import kh.edu.istad.platform.domain.entity.CreditHistory;

import java.util.List;
import java.util.UUID;

public interface CreditHistoryRepository {
    CreditHistory save(CreditHistory creditHistory);
    List<CreditHistory> findByCustomerId(UUID customerId);
}
