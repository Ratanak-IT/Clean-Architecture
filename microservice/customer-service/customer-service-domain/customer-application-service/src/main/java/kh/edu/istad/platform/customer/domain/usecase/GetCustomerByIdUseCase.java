package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetCustomerByIdUseCase {
    private final CustomerRepository customerRepository;

    public Customer execute(UUID customerId){
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerException("CUstomer with ID: "+ customerId+" not found"));
    }
}
