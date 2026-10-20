package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.CreateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentMonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreateCustomerSegmentUseCase {
    private final CustomerSegmentMonoRepository customerSegmentMonoRepository;

    public Mono<CustomerSegmentResult> execute(CreateCustomerSegmentCommand command){
        return customerSegmentMonoRepository.create(new CreateCustomerSegmentCommand(
                command.code().trim().toUpperCase(),
                command.name().trim()
        ));
    }
}
