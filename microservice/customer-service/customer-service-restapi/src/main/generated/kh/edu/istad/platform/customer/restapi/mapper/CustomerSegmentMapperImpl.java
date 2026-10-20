package kh.edu.istad.platform.customer.restapi.mapper;

import javax.annotation.processing.Generated;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.restapi.dto.CreateCustomerSegmentRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentResponse;
import kh.edu.istad.platform.customer.restapi.dto.UpdateCustomerSegmentRequest;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-20T16:14:59+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (JetBrains s.r.o.)"
)
@Component
public class CustomerSegmentMapperImpl implements CustomerSegmentMapper {

    @Override
    public CustomerSegmentResponse toResponse(CustomerSegmentResult customerSegmentResult) {
        if ( customerSegmentResult == null ) {
            return null;
        }

        String id = null;
        String code = null;
        String name = null;

        id = customerSegmentResult.id();
        code = customerSegmentResult.code();
        name = customerSegmentResult.name();

        CustomerSegmentResponse customerSegmentResponse = new CustomerSegmentResponse( id, code, name );

        return customerSegmentResponse;
    }

    @Override
    public UpdateCustomerSegmentCommand toCommand(UpdateCustomerSegmentRequest request) {
        if ( request == null ) {
            return null;
        }

        String code = null;
        String name = null;

        code = request.code();
        name = request.name();

        UpdateCustomerSegmentCommand updateCustomerSegmentCommand = new UpdateCustomerSegmentCommand( code, name );

        return updateCustomerSegmentCommand;
    }

    @Override
    public CreateCustomerSegmentCommand toCommand(CreateCustomerSegmentRequest request) {
        if ( request == null ) {
            return null;
        }

        String code = null;
        String name = null;

        code = request.code();
        name = request.name();

        CreateCustomerSegmentCommand createCustomerSegmentCommand = new CreateCustomerSegmentCommand( code, name );

        return createCustomerSegmentCommand;
    }
}
