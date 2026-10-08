package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.domain.dto.*;
import kh.edu.istad.platform.customer.restapi.dto.*;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerWebMapper {
    InitiateCustomerCommand toCommand(CustomerInitiateRequest request);
    CustomerInitiateResponse toResponse(InitiateCustomerResult result);
    CustomerDeactiveResponse toResponse(DeactiveCustomerResult result);
    UpdateCustomerCommand toCommand(UUID customerId, CustomerUpdateRequest request);
    CustomerUpdateResponse toResponse(UpdateCustomerResult result);

}
