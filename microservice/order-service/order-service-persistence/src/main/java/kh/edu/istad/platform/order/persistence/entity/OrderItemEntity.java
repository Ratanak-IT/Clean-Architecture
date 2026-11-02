package kh.edu.istad.platform.order.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItemEntity {

    @Id
    private OrderItemEntityId orderItemEntityId;
    @Column("order_id")
    private UUID orderId;

    @Column("product_id")
    private UUID productId;

    @Column("quantity")
    private int quantity;

    @Column("price")
    private BigDecimal price;

    @Column("sub_total")
    private BigDecimal subTotal;
}