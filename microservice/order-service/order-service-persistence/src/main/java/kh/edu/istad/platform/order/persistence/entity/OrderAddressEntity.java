package kh.edu.istad.platform.order.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("order_addresses")
@Getter
@Setter
@NoArgsConstructor
public class OrderAddressEntity {

    @Id
    private UUID id;


    @Column("street")
    private String street;

    @Column("postal_code")
    private String postalCode;

    @Column("city")
    private String city;
}