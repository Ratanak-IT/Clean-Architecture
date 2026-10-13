package kh.edu.istad.platform.business.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.business.persistence.mapper.BusinessPersistenceMapper;
import kh.edu.istad.platform.business.persistence.repository.BusinessMongoRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BusinessRepositoryAdapter implements BusinessRepository {

    private final BusinessMongoRepository mongoRepository;
    private final BusinessPersistenceMapper mapper;

    public BusinessRepositoryAdapter(BusinessMongoRepository mongoRepository, BusinessPersistenceMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Business> save(Business business) {
        return mongoRepository.save(mapper.toDocument(business))
                .map(mapper::toDomain)
                .onErrorMap(DuplicateKeyException.class,
                        e -> new BusinessDomainException("Business with this legal name already exists"));
    }

    @Override
    public Mono<Business> findById(BusinessId businessId) {
        return mongoRepository.findById(businessId.value().toString()).map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByLegalName(String legalName) {
        return mongoRepository.existsByLegalName(legalName);
    }
}
