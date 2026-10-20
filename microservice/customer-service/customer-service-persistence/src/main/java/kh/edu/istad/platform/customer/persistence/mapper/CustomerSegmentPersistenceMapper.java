package kh.edu.istad.platform.customer.persistence.mapper;

import kh.edu.istad.platform.customer.domain.dto.CreateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerSegmentPersistenceMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    CustomerSegmentDocument toDomain(CreateCustomerSegmentCommand command);

    CustomerSegmentResult toResult(CustomerSegmentDocument document);
}
