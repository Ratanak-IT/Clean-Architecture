package kh.edu.istad.platform.customer.persistence.adapter;

import jakarta.transaction.Transactional;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.persistence.entity.CustomerEntity;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerJpaRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional
public class CustomerRepositoryAdapter implements CustomerRepository{
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerMapper mapper;
    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = mapper.toEntity(customer);
        CustomerEntity savedEntity = customerJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Customer deactiveCustomer(UUID customerId) {
        CustomerEntity entity = customerJpaRepository.findById(customerId)
                .orElseThrow(() -> new CustomerException("Customer with ID: " + customerId +" not found"));
        Customer customer = mapper.toDomain(entity);
        customer.deactivateCustomer();
        CustomerEntity updated = customerJpaRepository.save(mapper.toEntity(customer));
        return mapper.toDomain(updated);
    }

    @Override
    public Customer updateCustomer(UUID customerId, UpdateCustomerCommand request) {
        CustomerEntity entity = customerJpaRepository.findById(customerId)
                .orElseThrow(() -> new CustomerException("Customer with id: " + customerId +" not found"));
        Customer customer = mapper.toDomain(entity);
        customer.updateCustomer(request.familyName(), request.givenName());
        CustomerEntity updatedEntity = customerJpaRepository.save(mapper.toEntity(customer));

        return mapper.toDomain(updatedEntity);
    }
}
