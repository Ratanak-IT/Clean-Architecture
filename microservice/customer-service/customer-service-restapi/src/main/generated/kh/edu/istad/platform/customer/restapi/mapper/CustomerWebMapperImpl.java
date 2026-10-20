package kh.edu.istad.platform.customer.restapi.mapper;

import java.util.UUID;
import javax.annotation.processing.Generated;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.dto.DeactiveCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.GetCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerByIdCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.restapi.dto.CreateCustomerRequest;
import kh.edu.istad.platform.customer.restapi.dto.CreateCustomerResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerDeactiveResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerGetResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateByIdRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-20T23:48:59+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (JetBrains s.r.o.)"
)
@Component
public class CustomerWebMapperImpl implements CustomerWebMapper {

    @Override
    public InitiateCustomerCommand toCommand(CustomerInitiateRequest request) {
        if ( request == null ) {
            return null;
        }

        String userName = null;
        String familyName = null;
        String givenName = null;
        String email = null;
        String phoneNumber = null;

        userName = request.userName();
        familyName = request.familyName();
        givenName = request.givenName();
        email = request.email();
        phoneNumber = request.phoneNumber();

        InitiateCustomerCommand initiateCustomerCommand = new InitiateCustomerCommand( userName, familyName, givenName, email, phoneNumber );

        return initiateCustomerCommand;
    }

    @Override
    public CustomerInitiateResponse toResponse(InitiateCustomerResult result) {
        if ( result == null ) {
            return null;
        }

        String customerId = null;
        String familyName = null;
        String givenName = null;
        String email = null;
        String phoneNumber = null;

        if ( result.customerId() != null ) {
            customerId = result.customerId().toString();
        }
        familyName = result.familyName();
        givenName = result.givenName();
        email = result.email();
        phoneNumber = result.phoneNumber();

        String username = null;

        CustomerInitiateResponse customerInitiateResponse = new CustomerInitiateResponse( customerId, username, familyName, givenName, email, phoneNumber );

        return customerInitiateResponse;
    }

    @Override
    public CustomerDeactiveResponse toResponse(DeactiveCustomerResult result) {
        if ( result == null ) {
            return null;
        }

        CustomerStatus status = null;

        status = result.status();

        CustomerDeactiveResponse customerDeactiveResponse = new CustomerDeactiveResponse( status );

        return customerDeactiveResponse;
    }

    @Override
    public UpdateCustomerCommand toCommand(UUID customerId, CustomerUpdateRequest request) {
        if ( customerId == null && request == null ) {
            return null;
        }

        String familyName = null;
        String givenName = null;
        if ( request != null ) {
            familyName = request.familyName();
            givenName = request.givenName();
        }
        UUID customerId1 = null;
        customerId1 = customerId;

        UpdateCustomerCommand updateCustomerCommand = new UpdateCustomerCommand( customerId1, familyName, givenName );

        return updateCustomerCommand;
    }

    @Override
    public CustomerUpdateResponse toResponse(UpdateCustomerResult result) {
        if ( result == null ) {
            return null;
        }

        UUID customerId = null;
        String username = null;
        String familyName = null;
        String givenName = null;
        String email = null;
        String phoneNumber = null;
        String customerStatus = null;

        customerId = result.customerId();
        username = result.username();
        familyName = result.familyName();
        givenName = result.givenName();
        email = result.email();
        phoneNumber = result.phoneNumber();
        if ( result.customerStatus() != null ) {
            customerStatus = result.customerStatus().name();
        }

        CustomerUpdateResponse customerUpdateResponse = new CustomerUpdateResponse( customerId, username, familyName, givenName, email, phoneNumber, customerStatus );

        return customerUpdateResponse;
    }

    @Override
    public CreateCustomerCommand toCommand(CreateCustomerRequest result) {
        if ( result == null ) {
            return null;
        }

        String familyName = null;
        String givenName = null;
        String username = null;
        String email = null;
        String phoneNumber = null;
        String segmentId = null;

        familyName = result.familyName();
        givenName = result.givenName();
        username = result.username();
        email = result.email();
        phoneNumber = result.phoneNumber();
        segmentId = result.segmentId();

        CreateCustomerCommand createCustomerCommand = new CreateCustomerCommand( familyName, givenName, username, email, phoneNumber, segmentId );

        return createCustomerCommand;
    }

    @Override
    public CreateCustomerResponse toResponse(CreateCustomerResult result) {
        if ( result == null ) {
            return null;
        }

        String familyName = null;
        String givenName = null;
        String username = null;
        String email = null;
        String phoneNumber = null;
        String segmentId = null;

        familyName = result.familyName();
        givenName = result.givenName();
        username = result.username();
        email = result.email();
        phoneNumber = result.phoneNumber();
        segmentId = result.segmentId();

        String customerId = null;
        String customerStatus = null;

        CreateCustomerResponse createCustomerResponse = new CreateCustomerResponse( customerId, familyName, givenName, username, email, phoneNumber, customerStatus, segmentId );

        return createCustomerResponse;
    }

    @Override
    public CustomerGetResponse toResponse(GetCustomerResult result) {
        if ( result == null ) {
            return null;
        }

        String customerId = null;
        String username = null;
        String familyName = null;
        String givenName = null;
        String email = null;
        String phoneNumber = null;
        CustomerStatus customerStatus = null;
        CustomerSegmentResponse segment = null;

        customerId = result.customerId();
        username = result.username();
        familyName = result.familyName();
        givenName = result.givenName();
        email = result.email();
        phoneNumber = result.phoneNumber();
        customerStatus = result.customerStatus();
        segment = customerSegmentResultToCustomerSegmentResponse( result.segment() );

        CustomerGetResponse customerGetResponse = new CustomerGetResponse( customerId, username, familyName, givenName, email, phoneNumber, customerStatus, segment );

        return customerGetResponse;
    }

    @Override
    public UpdateCustomerByIdCommand toCommand(CustomerUpdateByIdRequest request) {
        if ( request == null ) {
            return null;
        }

        String familyName = null;
        String givenName = null;
        String phoneNumber = null;

        familyName = request.familyName();
        givenName = request.givenName();
        phoneNumber = request.phoneNumber();

        UpdateCustomerByIdCommand updateCustomerByIdCommand = new UpdateCustomerByIdCommand( familyName, givenName, phoneNumber );

        return updateCustomerByIdCommand;
    }

    protected CustomerSegmentResponse customerSegmentResultToCustomerSegmentResponse(CustomerSegmentResult customerSegmentResult) {
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
}
