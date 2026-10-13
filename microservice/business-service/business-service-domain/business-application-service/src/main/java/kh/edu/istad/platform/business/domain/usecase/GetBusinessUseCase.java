package kh.edu.istad.platform.business.domain.usecase;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.domain.dto.BusinessResult;
import kh.edu.istad.platform.business.domain.mapper.BusinessMapper;
import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GetBusinessUseCase {

    private final BusinessRepository businessRepository;
    private final BusinessMapper mapper;

    public GetBusinessUseCase(BusinessRepository businessRepository, BusinessMapper mapper) {
        this.businessRepository = businessRepository;
        this.mapper = mapper;
    }

    public Mono<BusinessResult> execute(UUID businessId) {
        return businessRepository.findById(new BusinessId(businessId))
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException("Business", businessId)))
                .map(mapper::toResult);
    }
}
