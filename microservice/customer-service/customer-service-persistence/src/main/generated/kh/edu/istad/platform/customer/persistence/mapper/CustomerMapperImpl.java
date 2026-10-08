package kh.edu.istad.platform.customer.persistence.mapper;

import java.util.UUID;
import javax.annotation.processing.Generated;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.persistence.entity.CustomerEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-08T23:18:33+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (JetBrains s.r.o.)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerEntity toEntity(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setCustomerId( customerIdValue( customer ) );
        customerEntity.setEmail( customerEmailValue( customer ) );
        customerEntity.setPhoneNumber( customerPhoneNumberValue( customer ) );
        customerEntity.setUsername( customer.getUsername() );
        customerEntity.setFamilyName( customer.getFamilyName() );
        customerEntity.setGivenName( customer.getGivenName() );
        customerEntity.setCustomerStatus( customer.getCustomerStatus() );

        return customerEntity;
    }

    private UUID customerIdValue(Customer customer) {
        CustomerId id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id.value();
    }

    private String customerEmailValue(Customer customer) {
        Email email = customer.getEmail();
        if ( email == null ) {
            return null;
        }
        return email.value();
    }

    private String customerPhoneNumberValue(Customer customer) {
        PhoneNumber phoneNumber = customer.getPhoneNumber();
        if ( phoneNumber == null ) {
            return null;
        }
        return phoneNumber.value();
    }
}
