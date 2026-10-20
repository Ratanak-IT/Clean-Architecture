package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.domain.dto.CustomerStatisticResult;
import kh.edu.istad.platform.customer.restapi.dto.CustomerStatisticResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CustomerSegmentMapper.class)
public interface CustomerStatisticMapper {
    CustomerStatisticResponse toResponse(CustomerStatisticResult result);
}