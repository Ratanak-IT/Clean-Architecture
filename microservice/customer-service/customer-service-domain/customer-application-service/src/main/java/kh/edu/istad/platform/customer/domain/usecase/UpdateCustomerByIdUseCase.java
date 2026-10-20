package kh.edu.istad.platform.customer.domain.usecase;


import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerByIdCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.port.out.CustomerMonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateCustomerByIdUseCase {
    private final CustomerMonoRepository customerMonoRepository;

    public Mono<UpdateCustomerResult> execute(CustomerId id, UpdateCustomerByIdCommand command){
        return customerMonoRepository.updateById(id, command);
    }
}
