package kh.edu.istad.platform.customer.domain.mapper;

import javax.annotation.processing.Generated;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-20T23:41:55+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (JetBrains s.r.o.)"
)
@Component
public class CreateCustomerMapperImpl implements CreateCustomerMapper {

    @Override
    public Customer toDomain(CreateCustomerCommand customerCommand) {
        if ( customerCommand == null ) {
            return null;
        }

        PhoneNumber phoneNumber = null;
        String username = null;
        Email email = null;
        String familyName = null;
        String givenName = null;
        String segmentId = null;

        phoneNumber = createCustomerCommandToPhoneNumber( customerCommand );
        username = customerCommand.username();
        email = toEmail( customerCommand.email() );
        familyName = customerCommand.familyName();
        givenName = customerCommand.givenName();
        segmentId = customerCommand.segmentId();

        CustomerStatus customerStatus = null;

        Customer customer = new Customer( username, email, phoneNumber, familyName, givenName, customerStatus, segmentId );

        return customer;
    }

    @Override
    public CreateCustomerResult toResult(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        String familyName = null;
        String givenName = null;
        String username = null;
        String email = null;
        String phoneNumber = null;
        String segmentId = null;

        familyName = customer.getFamilyName();
        givenName = customer.getGivenName();
        username = customer.getUsername();
        email = fromEmail( customer.getEmail() );
        phoneNumber = fromPhoneNumber( customer.getPhoneNumber() );
        segmentId = customer.getSegmentId();

        CreateCustomerResult createCustomerResult = new CreateCustomerResult( familyName, givenName, username, email, phoneNumber, segmentId );

        return createCustomerResult;
    }

    protected PhoneNumber createCustomerCommandToPhoneNumber(CreateCustomerCommand createCustomerCommand) {
        if ( createCustomerCommand == null ) {
            return null;
        }

        String value = null;

        value = createCustomerCommand.phoneNumber();

        PhoneNumber phoneNumber = new PhoneNumber( value );

        return phoneNumber;
    }
}
