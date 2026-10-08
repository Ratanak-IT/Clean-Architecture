package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;

import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);
    Customer deactiveCustomer(UUID customerId);
    Customer updateCustomer(UUID customerId, UpdateCustomerCommand request);
}