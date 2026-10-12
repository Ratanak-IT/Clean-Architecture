package kh.edu.istad.platform.customer.persistence.document;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Document
public class CustomerDocument {
    @Id
    private CustomerId customerId;
    private String username;
    private String familyName;
    private String givenName;
    private Email email;
    private PhoneNumber phoneNumber;
    private CustomerStatus customerStatus;
}
