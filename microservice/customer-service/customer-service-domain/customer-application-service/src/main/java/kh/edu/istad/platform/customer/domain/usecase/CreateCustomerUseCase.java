package kh.edu.istad.platform.customer.domain.usecase;


import kh.edu.istad.platform.customer.domain.dto.CreateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.event.CustomerInitiatedEvent;
import kh.edu.istad.platform.customer.domain.mapper.CreateCustomerMapper;
import kh.edu.istad.platform.customer.domain.port.out.CustomerMonoRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreateCustomerUseCase {
    private final CustomerMonoRepository customerRepository;
//    private final CustomerDomainService customerDomainService;
    private final CreateCustomerMapper mapper;

    public Mono<CreateCustomerResult> execute(CreateCustomerCommand customerCommand){
        Customer createCustomer = mapper.toDomain(customerCommand);
        createCustomer.initiateCustomer();
//        CustomerInitiatedEvent event = customerDomainService.initiatedCustomer(createCustomer);
        return customerRepository.save(createCustomer);
    }
}
