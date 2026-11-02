package kh.edu.istad.platform.order.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.persistence.entity.OrderAddressEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class OrderPersistenceMapper {

    private static final String FAILURE_MESSAGE_DELIMITER = ",";

    public OrderEntity toEntity(Order order) {
        if (order == null) return null;

        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId() != null ? order.getId().value() : null);
        entity.setCustomerId(order.getCustomerId() != null ? order.getCustomerId().value() : null);
        entity.setBusinessId(order.getBusinessId() != null ? order.getBusinessId().value() : null);
        entity.setPrice(order.getPrice() != null ? order.getPrice().amount() : null);
        entity.setTrackingId(order.getTrackingId() != null ? order.getTrackingId().value() : null);

        entity.setOrderStatus(order.getOrderStatus() != null ? order.getOrderStatus().name() : null);

        // 2. List<String> -> String (joined by delimiter)
        entity.setFailureMessages(toFailureMessagesString(order.getFailureMessages()));

        if (order.getDeliveryAddress() != null) {
            entity.setOrderAddressId(order.getDeliveryAddress().id());
        }

        return entity;
    }

    public OrderAddressEntity toAddressEntity(Order order) {
        if (order == null || order.getDeliveryAddress() == null) return null;

        StreetAddress address = order.getDeliveryAddress();
        OrderAddressEntity e = new OrderAddressEntity();
        e.setId(address.id());
        e.setStreet(address.street());
        e.setPostalCode(address.postalCode());
        e.setCity(address.city());
        return e;
    }

    public List<OrderItemEntity> toItemEntities(Order order) {
        if (order == null || order.getItems() == null) return new ArrayList<>();

        List<OrderItemEntity> list = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemEntity e = new OrderItemEntity();
            e.setOrderId(order.getId() != null ? order.getId().value() : null);
            e.setProductId(item.getProduct().getId().value());
            e.setQuantity(item.getQuantity());
            e.setPrice(item.getPrice().amount());
            e.setSubTotal(item.getSubTotal().amount());
            list.add(e);
        }
        return list;
    }

    public Order toDomain(OrderEntity entity, OrderAddressEntity addressEntity, List<OrderItemEntity> itemEntities) {
        OrderId orderId = new OrderId(entity.getId());

        List<OrderItem> items = itemEntities == null ? new ArrayList<>() : itemEntities.stream()
                .map(itemEntity -> OrderItem.Builder.newBuilder()
                        .orderId(orderId)
                        .product(new Product(
                                new ProductId(itemEntity.getProductId()),
                                "", // Product name is in catalog/product domain
                                new Money(itemEntity.getPrice())))
                        .quantity(itemEntity.getQuantity())
                        .price(new Money(itemEntity.getPrice()))
                        .subTotal(new Money(itemEntity.getSubTotal()))
                        .build())
                .toList();

        StreetAddress deliveryAddress = addressEntity != null
                ? new StreetAddress(addressEntity.getId(), addressEntity.getStreet(), addressEntity.getPostalCode(), addressEntity.getCity())
                : null;

        return Order.Builder.newBuilder()
                .id(orderId)
                .customerId(new CustomerId(entity.getCustomerId()))
                .businessId(new BusinessId(entity.getBusinessId()))
                .deliveryAddress(deliveryAddress)
                .price(new Money(entity.getPrice()))
                .items(items)
                .trackingId(new TrackingId(entity.getTrackingId()))
                // 1. String -> OrderStatus (Enum)
                .orderStatus(entity.getOrderStatus() != null ? OrderStatus.valueOf(entity.getOrderStatus()) : null)
                // 2. String -> List<String>
                .failureMessages(toFailureMessagesList(entity.getFailureMessages()))
                .build();
    }

    private String toFailureMessagesString(List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return "";
        }
        return String.join(FAILURE_MESSAGE_DELIMITER, messages);
    }

    private List<String> toFailureMessagesList(String messages) {
        if (messages == null || messages.isBlank()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(messages.split(FAILURE_MESSAGE_DELIMITER)));
    }
}
