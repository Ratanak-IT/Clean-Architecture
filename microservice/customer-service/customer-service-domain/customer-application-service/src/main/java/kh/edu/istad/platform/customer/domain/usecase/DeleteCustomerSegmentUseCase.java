package kh.edu.istad.platform.customer.domain.usecase;


import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentMonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DeleteCustomerSegmentUseCase {

    private final CustomerSegmentMonoRepository repository;

    public Mono<Void> execute(String id){
        return repository.delete(id);
    }
}
