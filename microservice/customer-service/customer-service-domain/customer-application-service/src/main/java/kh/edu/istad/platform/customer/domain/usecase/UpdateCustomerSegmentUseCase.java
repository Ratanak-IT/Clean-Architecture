package kh.edu.istad.platform.customer.domain.usecase;


import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentMonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateCustomerSegmentUseCase {
    private final CustomerSegmentMonoRepository repository;

    public Mono<CustomerSegmentResult> execute(String id, UpdateCustomerSegmentCommand command) {
        return repository.update(id, new UpdateCustomerSegmentCommand(
                command.code().trim().toUpperCase(),
                command.name().trim()));
    }
}
