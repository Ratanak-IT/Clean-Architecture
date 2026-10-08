package kh.edu.istad.platform.customer.domain.usecase;


import kh.edu.istad.platform.customer.domain.dto.DeactiveCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.DeactiveCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeactiveCustomerUseCase {
    private final CustomerRepository customerRepository;

    public DeactiveCustomerResult execute(DeactiveCustomerCommand command){
        log.info("Executing deactivate customer usecase for ID: {}", command.customerId());
        Customer deactivatedCustomer = customerRepository.deactiveCustomer(command.customerId());

        log.info("Customer deactivated successfully: {}", deactivatedCustomer.getId());
        return new DeactiveCustomerResult(deactivatedCustomer.getCustomerStatus());
    }
}
