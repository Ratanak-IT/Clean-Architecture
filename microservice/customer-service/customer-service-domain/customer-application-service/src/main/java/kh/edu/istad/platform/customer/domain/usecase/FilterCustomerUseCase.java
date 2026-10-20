package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.query.FilterQuery;
import kh.edu.istad.platform.customer.domain.dto.GetCustomerResult;
import kh.edu.istad.platform.customer.domain.port.out.CustomerMonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class FilterCustomerUseCase {
    private final CustomerMonoRepository customerMonoRepository;

    public Flux<GetCustomerResult> execute(FilterQuery query){
        return customerMonoRepository.filter(query);
    }

}
