package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentMonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GetCustomerSegmentByIdUseCase {
    private final CustomerSegmentMonoRepository customerSegmentMonoRepository;

    public Mono<CustomerSegmentResult> execute(String id){
        return customerSegmentMonoRepository.findById(id);
    }
}
