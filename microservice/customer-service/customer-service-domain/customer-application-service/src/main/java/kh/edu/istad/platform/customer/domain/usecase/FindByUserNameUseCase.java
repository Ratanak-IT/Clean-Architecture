package kh.edu.istad.platform.customer.domain.usecase;


import kh.edu.istad.platform.customer.domain.dto.GetCustomerResult;
import kh.edu.istad.platform.customer.domain.port.out.CustomerMonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FindByUserNameUseCase {
    private final CustomerMonoRepository customerMonoRepository;

    public Mono<GetCustomerResult> execute(String userName){
        return customerMonoRepository.findByUsername(userName);
    }
}
