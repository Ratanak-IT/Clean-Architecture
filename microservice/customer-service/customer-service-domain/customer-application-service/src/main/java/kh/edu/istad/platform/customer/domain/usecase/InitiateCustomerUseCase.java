package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.mapper.InitiateCustomerMapper;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;




// one use case have only one logic
@Component
@Slf4j
@RequiredArgsConstructor
public class InitiateCustomerUseCase {
    private final CustomerRepository customerRepository;
    private final InitiateCustomerMapper initiateCustomerMapper;
    public InitiateCustomerResult execute(InitiateCustomerCommand command){
        log.info("Initiating customer usecase with command: {}", command);
        Customer customer = initiateCustomerMapper.toCustomer(command);
        customer.initiateCustomer();
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer initiated and saved successfully with ID: {}", savedCustomer.getId().value());
        return initiateCustomerMapper.toResult(savedCustomer);
    }
}
