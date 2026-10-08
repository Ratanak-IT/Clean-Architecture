package kh.edu.istad.platform.customer.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.persistence.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "id.value", target = "customerId")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "phoneNumber.value", target = "phoneNumber")
    CustomerEntity toEntity(Customer customer);

    default Customer toDomain(CustomerEntity entity) {
        if (entity == null) return null;

        return Customer.Builder.builder()
                .id(entity.getCustomerId() != null ? new CustomerId(entity.getCustomerId()) : null)
                .username(entity.getUsername())
                .familyName(entity.getFamilyName())
                .givenName(entity.getGivenName())
                .email(entity.getEmail() != null ? new Email(entity.getEmail()) : null)
                .phoneNumber(entity.getPhoneNumber() != null ? new PhoneNumber(entity.getPhoneNumber()) : null)
                .customerStatus(entity.getCustomerStatus())
                .build();
    }
}