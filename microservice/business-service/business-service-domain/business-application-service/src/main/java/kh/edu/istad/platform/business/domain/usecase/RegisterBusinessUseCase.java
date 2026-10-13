package kh.edu.istad.platform.business.domain.usecase;

import kh.edu.istad.platform.business.domain.dto.BusinessResult;
import kh.edu.istad.platform.business.domain.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.mapper.BusinessMapper;
import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.business.domain.service.BusinessDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RegisterBusinessUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegisterBusinessUseCase.class);

    private final BusinessRepository businessRepository;
    private final BusinessDomainService businessDomainService;
    private final BusinessMapper mapper;

    public RegisterBusinessUseCase(BusinessRepository businessRepository,
                                   BusinessDomainService businessDomainService,
                                   BusinessMapper mapper) {
        this.businessRepository = businessRepository;
        this.businessDomainService = businessDomainService;
        this.mapper = mapper;
    }

    public Mono<BusinessResult> execute(RegisterBusinessCommand command) {
        return Mono.defer(() -> {
            Business business = mapper.toBusiness(command);
            BusinessRegisteredEvent event = businessDomainService.validateAndInitiateBusiness(business);
            String legalName = business.getBusinessName().legalName();
            log.info("Registering business '{}'", legalName);
            return businessRepository.existsByLegalName(legalName)
                    .flatMap(exists -> exists
                            ? Mono.<Business>error(new BusinessDomainException(
                                    "Business with legal name '" + legalName + "' already exists"))
                            : businessRepository.save(business))
                    .map(saved -> {
                        log.info("Business {} registered ({} at {})", saved.getId().value(),
                                event.getClass().getSimpleName(), event.getCreatedAt());
                        return mapper.toResult(saved);
                    });
        });
    }
}
