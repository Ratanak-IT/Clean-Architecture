package kh.edu.istad.platform.customer.domain.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UpdateCustomerMapper {
    public Customer toCustomer(UpdateCustomerCommand command) {
        if (command == null) return null;

        return Customer.Builder.builder()
                .familyName(command.familyName())
                .givenName(command.givenName())
                .build();
    }

    public UpdateCustomerResult toResult(Customer customer) {
        if (customer == null) return null;

        return new UpdateCustomerResult(
                customer.getId() != null ? customer.getId().value() : null,
                customer.getUsername(),
                customer.getFamilyName(),
                customer.getGivenName(),
                customer.getEmail() != null ? customer.getEmail().value() : null,
                customer.getPhoneNumber() != null ? customer.getPhoneNumber().value() : null
        );
    }
}
