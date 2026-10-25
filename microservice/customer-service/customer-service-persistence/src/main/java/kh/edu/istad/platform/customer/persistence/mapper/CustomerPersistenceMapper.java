package kh.edu.istad.platform.customer.persistence.mapper;


import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.GetCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerByIdCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {


    @Named("segmentRef")
    default CustomerSegmentDocument segmentRef(CustomerSegmentId segmentId) {
        if (segmentId == null) return null;
        CustomerSegmentDocument segment = new CustomerSegmentDocument();
        segment.setId(segmentId.value().toString());
        return segment;
    }

    default String fromEmail(Email email) {
        return email != null ? email.value() : null;
    }

    default String fromPhone(PhoneNumber phone) {
        return phone != null ? phone.value() : null;
    }

    default PhoneNumber toPhone(String phone){
        return phone != null? new PhoneNumber(phone) : null;
    }
    default Email toEmail(String email){
        return email !=null ? new Email(email): null;
    }
    @Mapping(source = "id.value", target = "customerId")
    @Mapping(source = "segmentId", target = "segment", qualifiedByName = "segmentRef")
    CustomerDocument toDocument(Customer customer);

    @Mapping(source = "segment.id", target = "segmentId")
    CreateCustomerResult toDomain(CustomerDocument customerDocument);

    GetCustomerResult toGetResult(CustomerDocument document);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDocument(UpdateCustomerByIdCommand command, @MappingTarget CustomerDocument document);

    UpdateCustomerResult toUpdateResult(CustomerDocument document);
}
