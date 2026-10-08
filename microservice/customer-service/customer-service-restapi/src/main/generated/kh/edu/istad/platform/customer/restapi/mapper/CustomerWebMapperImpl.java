package kh.edu.istad.platform.customer.restapi.mapper;

import java.util.UUID;
import javax.annotation.processing.Generated;
import kh.edu.istad.platform.customer.domain.dto.DeactiveCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.restapi.dto.CustomerDeactiveResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-08T23:38:55+0700",
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
        String userName = null;
        String familyName = null;
        String givenName = null;
        String email = null;
        String phoneNumber = null;

        if ( result.customerId() != null ) {
            customerId = result.customerId().toString();
        }
        userName = result.userName();
        familyName = result.familyName();
        givenName = result.givenName();
        email = result.email();
        phoneNumber = result.phoneNumber();

        CustomerInitiateResponse customerInitiateResponse = new CustomerInitiateResponse( customerId, userName, familyName, givenName, email, phoneNumber );

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

        customerId = result.customerId();
        username = result.username();
        familyName = result.familyName();
        givenName = result.givenName();
        email = result.email();
        phoneNumber = result.phoneNumber();

        String customerStatus = null;

        CustomerUpdateResponse customerUpdateResponse = new CustomerUpdateResponse( customerId, username, familyName, givenName, email, phoneNumber, customerStatus );

        return customerUpdateResponse;
    }
}
