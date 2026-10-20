package kh.edu.istad.platform.customer.persistence.mapper;

import javax.annotation.processing.Generated;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-20T16:23:03+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (JetBrains s.r.o.)"
)
@Component
public class CustomerSegmentPersistenceMapperImpl implements CustomerSegmentPersistenceMapper {

    @Override
    public CustomerSegmentDocument toDomain(CreateCustomerSegmentCommand command) {
        if ( command == null ) {
            return null;
        }

        CustomerSegmentDocument customerSegmentDocument = new CustomerSegmentDocument();

        customerSegmentDocument.setCode( command.code() );
        customerSegmentDocument.setName( command.name() );

        customerSegmentDocument.setId( java.util.UUID.randomUUID().toString() );

        return customerSegmentDocument;
    }

    @Override
    public CustomerSegmentResult toResult(CustomerSegmentDocument document) {
        if ( document == null ) {
            return null;
        }

        String id = null;
        String code = null;
        String name = null;

        id = document.getId();
        code = document.getCode();
        name = document.getName();

        CustomerSegmentResult customerSegmentResult = new CustomerSegmentResult( id, code, name );

        return customerSegmentResult;
    }
}
