package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.domain.dto.*;
import kh.edu.istad.platform.customer.restapi.dto.*;
import org.mapstruct.Mapper;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerWebMapper {
    InitiateCustomerCommand toCommand(CustomerInitiateRequest request);
    CustomerInitiateResponse toResponse(InitiateCustomerResult result);
    CustomerDeactiveResponse toResponse(DeactiveCustomerResult result);
    UpdateCustomerCommand toCommand(UUID customerId, CustomerUpdateRequest request);
    CustomerUpdateResponse toResponse(UpdateCustomerResult result);
//    CustomerInitiateResponse toResponse(CreateCustomerResult result);
    CreateCustomerCommand toCommand(CreateCustomerRequest result);
    CreateCustomerResponse toResponse(CreateCustomerResult result);

    CustomerGetResponse toResponse(GetCustomerResult result);

    UpdateCustomerByIdCommand toCommand(CustomerUpdateByIdRequest request);

}
