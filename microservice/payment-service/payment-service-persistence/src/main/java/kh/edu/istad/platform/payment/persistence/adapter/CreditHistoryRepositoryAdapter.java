package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.persistence.entity.CreditHistoryEntity;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.CreditHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional
public class CreditHistoryRepositoryAdapter implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        CreditHistoryEntity entity = mapper.toEntity(creditHistory);
        CreditHistoryEntity savedEntity = creditHistoryJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<CreditHistory> findByCustomerId(UUID customerId) {
        return creditHistoryJpaRepository.findByCustomerId(customerId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
