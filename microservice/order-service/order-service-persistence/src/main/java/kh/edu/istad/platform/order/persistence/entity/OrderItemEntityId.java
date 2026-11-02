package kh.edu.istad.platform.order.persistence.entity;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class OrderItemEntityId {
    private Integer id;
    private UUID orderId;
}
