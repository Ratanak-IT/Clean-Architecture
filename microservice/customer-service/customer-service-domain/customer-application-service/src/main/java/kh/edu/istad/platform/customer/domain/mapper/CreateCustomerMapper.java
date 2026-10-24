package kh.edu.istad.platform.customer.domain.mapper;


import kh.edu.istad.platform.customer.domain.dto.CreateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CreateCustomerMapper {

     @Mapping(source = "phoneNumber" , target = "phoneNumber.value")
     @Mapping(source = "segmentId", target = "segmentId.value")
     Customer toDomain(CreateCustomerCommand customerCommand);

     @Mapping(source = "segmentId.value", target = "segmentId")
     CreateCustomerResult toResult(Customer customer);

     default PhoneNumber toPhoneNumber(String phoneNumber){
          return new PhoneNumber(phoneNumber);
     }
     default Email toEmail(String email){
          return new Email(email);
     }

     default String fromPhoneNumber(PhoneNumber phoneNumber) {
          if (phoneNumber == null) {
               return null;
          }
          return phoneNumber.value();
     }

     default String fromEmail(Email email) {
          if (email == null) {
               return null;
          }
          return email.value();
     }
}
