package kh.edu.istad.platform.customer.persistence.document;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Document
@Getter
@Setter
@Builder
public class CustomerDocument implements Persistable<String> {
    @Id
    private String customerId;
    private String username;
    private String familyName;
    private String givenName;
    private Email email;
    private PhoneNumber phoneNumber;
    private CustomerStatus customerStatus;
    @DocumentReference
    private CustomerSegmentDocument segment;

    @CreatedBy
    private String createdBy;
    @CreatedDate
    private Instant createdAt;

    @LastModifiedBy
    private String modifiedBy;
    @LastModifiedDate
    private Instant modifiedAt;


    @Override
    public @Nullable String getId() {
        return customerId;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
