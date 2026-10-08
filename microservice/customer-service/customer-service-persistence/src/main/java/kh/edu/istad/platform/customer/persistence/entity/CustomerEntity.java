package kh.edu.istad.platform.customer.persistence.entity;


import jakarta.persistence.*;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @Column(name = "customer_id", updatable = false, nullable = false)
    private UUID customerId;
    @Column(nullable = false, unique = true)
    private String username;
    private String familyName;
    private String givenName;
    @Column(nullable = false, unique = true)
    private String email;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus customerStatus;


}
