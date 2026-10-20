package kh.edu.istad.platform.customer.restapi.mapper;


import kh.edu.istad.platform.customer.domain.dto.CreateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.restapi.dto.CreateCustomerSegmentRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentResponse;
import kh.edu.istad.platform.customer.restapi.dto.UpdateCustomerSegmentRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerSegmentMapper {

    CustomerSegmentResponse toResponse(CustomerSegmentResult customerSegmentResult);

    UpdateCustomerSegmentCommand toCommand(UpdateCustomerSegmentRequest request);
    CreateCustomerSegmentCommand toCommand(CreateCustomerSegmentRequest request);
}
