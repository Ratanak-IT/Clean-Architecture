package kh.edu.istad.platform.customer.domain.usecase;


import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.port.out.CustomerMonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DeleteCustomerByIdUseCase {
    private final CustomerMonoRepository customerMonoRepository;

    public Mono<Void> execute(CustomerId id){
        return customerMonoRepository.deleteById(id);
    }
}
