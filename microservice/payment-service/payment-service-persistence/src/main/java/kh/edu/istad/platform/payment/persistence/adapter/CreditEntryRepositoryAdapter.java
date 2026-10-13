package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.persistence.entity.CreditEntryEntity;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.CreditEntryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional
public class CreditEntryRepositoryAdapter implements CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        CreditEntryEntity entity = mapper.toEntity(creditEntry);
        CreditEntryEntity savedEntity = creditEntryJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(UUID customerId) {
        return creditEntryJpaRepository.findByCustomerId(customerId).map(mapper::toDomain);
    }
}
