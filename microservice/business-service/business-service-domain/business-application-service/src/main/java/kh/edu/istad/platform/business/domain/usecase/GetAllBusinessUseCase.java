package kh.edu.istad.platform.business.domain.usecase;

import kh.edu.istad.platform.business.domain.dto.BusinessResult;
import kh.edu.istad.platform.business.domain.mapper.BusinessMapper;
import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class GetAllBusinessUseCase {
    private final BusinessRepository repository;
    private final BusinessMapper mapper;
    public Flux<BusinessResult> execute(){
        return repository.findAll().map(mapper::toResult);
    }
}
