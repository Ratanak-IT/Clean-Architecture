package kh.edu.istad.platform.customer.domain.mapper;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class InitiateCustomerMapper {

    public Customer toCustomer(InitiateCustomerCommand command) {
        if (command == null) return null;

        return Customer.Builder.builder()
                .username(command.userName())
                .familyName(command.familyName())
                .givenName(command.givenName())
                .email(new Email(command.email()))
                .phoneNumber(new PhoneNumber(command.phoneNumber())).build();
    }

    public InitiateCustomerResult toResult(Customer customer) {
        if (customer == null) return null;

        return new InitiateCustomerResult(
                customer.getId() != null ? customer.getId().value() : null,
                customer.getUsername(),
                customer.getFamilyName(),
                customer.getGivenName(),
                customer.getEmail() != null ? customer.getEmail().value() : null,
                customer.getPhoneNumber() != null ? customer.getPhoneNumber().value() : null
        );
    }
}