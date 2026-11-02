package kh.edu.istad.platform.order.persistence.entity;

import kh.edu.istad.common.domain.valueobject.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("orders")
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity {

    @Id
    private UUID id;

    @Column("business_id")
    private UUID businessId;

    @Column("customer_id")
    private UUID customerId;


    @Column("price")
    private BigDecimal price;

    @Column("tracking_id")
    private UUID trackingId;

    @Column("order_status")
    private String orderStatus;

    @Column("failure_messages")
    private String failureMessages;

    @Column("order_address_id")
    private UUID orderAddressId;
}