package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentMonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class GetCustomerSegmentsUseCase {
    private final CustomerSegmentMonoRepository customerSegmentMonoRepository;

    public Flux<CustomerSegmentResult> execute(){
        return customerSegmentMonoRepository.findAll();
    }
}
