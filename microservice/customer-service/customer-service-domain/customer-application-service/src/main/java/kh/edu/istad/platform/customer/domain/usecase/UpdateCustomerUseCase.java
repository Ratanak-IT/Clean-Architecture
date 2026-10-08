package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.mapper.UpdateCustomerMapper;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateCustomerUseCase {
    private final CustomerRepository customerRepository;
    private final UpdateCustomerMapper updateCustomerMapper;
    public UpdateCustomerResult execute(UpdateCustomerCommand command){
        log.info("Executing update customer usecase for ID: {}", command.customerId());
        Customer updatedCustomer = customerRepository.updateCustomer(command.customerId(), command);
        log.info("Customer updated successfully: {}", updatedCustomer.getId().value());
        return updateCustomerMapper.toResult(updatedCustomer);
    }
}
