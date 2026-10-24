package kh.edu.istad.platform.customer.persistence.mapper;

import java.util.UUID;
import javax.annotation.processing.Generated;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.dto.GetCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerByIdCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-24T00:36:36+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (JetBrains s.r.o.)"
)
@Component
public class CustomerPersistenceMapperImpl implements CustomerPersistenceMapper {

    @Override
    public CustomerDocument toDocument(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDocument.CustomerDocumentBuilder customerDocument = CustomerDocument.builder();

        UUID value = customerIdValue( customer );
        if ( value != null ) {
            customerDocument.customerId( value.toString() );
        }
        customerDocument.segment( segmentRef( customer.getSegmentId() ) );
        customerDocument.username( customer.getUsername() );
        customerDocument.familyName( customer.getFamilyName() );
        customerDocument.givenName( customer.getGivenName() );
        customerDocument.email( customer.getEmail() );
        customerDocument.phoneNumber( customer.getPhoneNumber() );
        customerDocument.customerStatus( customer.getCustomerStatus() );

        return customerDocument.build();
    }

    @Override
    public CreateCustomerResult toDomain(CustomerDocument customerDocument) {
        if ( customerDocument == null ) {
            return null;
        }

        String segmentId = null;
        String familyName = null;
        String givenName = null;
        String username = null;
        String email = null;
        String phoneNumber = null;

        segmentId = customerDocumentSegmentId( customerDocument );
        familyName = customerDocument.getFamilyName();
        givenName = customerDocument.getGivenName();
        username = customerDocument.getUsername();
        email = fromEmail( customerDocument.getEmail() );
        phoneNumber = fromPhone( customerDocument.getPhoneNumber() );

        CreateCustomerResult createCustomerResult = new CreateCustomerResult( familyName, givenName, username, email, phoneNumber, segmentId );

        return createCustomerResult;
    }

    @Override
    public GetCustomerResult toGetResult(CustomerDocument document) {
        if ( document == null ) {
            return null;
        }

        String customerId = null;
        String familyName = null;
        String givenName = null;
        String username = null;
        String email = null;
        String phoneNumber = null;
        CustomerStatus customerStatus = null;
        CustomerSegmentResult segment = null;

        customerId = document.getCustomerId();
        familyName = document.getFamilyName();
        givenName = document.getGivenName();
        username = document.getUsername();
        email = fromEmail( document.getEmail() );
        phoneNumber = fromPhone( document.getPhoneNumber() );
        customerStatus = document.getCustomerStatus();
        segment = customerSegmentDocumentToCustomerSegmentResult( document.getSegment() );

        GetCustomerResult getCustomerResult = new GetCustomerResult( customerId, familyName, givenName, username, email, phoneNumber, customerStatus, segment );

        return getCustomerResult;
    }

    @Override
    public void updateDocument(UpdateCustomerByIdCommand command, CustomerDocument document) {
        if ( command == null ) {
            return;
        }

        if ( command.familyName() != null ) {
            document.setFamilyName( command.familyName() );
        }
        if ( command.givenName() != null ) {
            document.setGivenName( command.givenName() );
        }
        if ( command.phoneNumber() != null ) {
            document.setPhoneNumber( toPhone( command.phoneNumber() ) );
        }
    }

    @Override
    public UpdateCustomerResult toUpdateResult(CustomerDocument document) {
        if ( document == null ) {
            return null;
        }

        UUID customerId = null;
        String username = null;
        String familyName = null;
        String givenName = null;
        String email = null;
        String phoneNumber = null;
        CustomerStatus customerStatus = null;

        if ( document.getCustomerId() != null ) {
            customerId = UUID.fromString( document.getCustomerId() );
        }
        username = document.getUsername();
        familyName = document.getFamilyName();
        givenName = document.getGivenName();
        email = fromEmail( document.getEmail() );
        phoneNumber = fromPhone( document.getPhoneNumber() );
        customerStatus = document.getCustomerStatus();

        UpdateCustomerResult updateCustomerResult = new UpdateCustomerResult( customerId, username, familyName, givenName, email, phoneNumber, customerStatus );

        return updateCustomerResult;
    }

    private UUID customerIdValue(Customer customer) {
        CustomerId id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id.value();
    }

    private String customerDocumentSegmentId(CustomerDocument customerDocument) {
        CustomerSegmentDocument segment = customerDocument.getSegment();
        if ( segment == null ) {
            return null;
        }
        return segment.getId();
    }

    protected CustomerSegmentResult customerSegmentDocumentToCustomerSegmentResult(CustomerSegmentDocument customerSegmentDocument) {
        if ( customerSegmentDocument == null ) {
            return null;
        }

        String id = null;
        String code = null;
        String name = null;

        id = customerSegmentDocument.getId();
        code = customerSegmentDocument.getCode();
        name = customerSegmentDocument.getName();

        CustomerSegmentResult customerSegmentResult = new CustomerSegmentResult( id, code, name );

        return customerSegmentResult;
    }
}
