package kh.edu.istad.platform.order.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.OrderItemId;
import kh.edu.istad.common.domain.valueobject.TrackingId;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.common.domain.valueobject.StreetAddress;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.persistence.entity.OrderAddressEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderPersistenceMapper {

    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId().value());
        entity.setCustomerId(order.getCustomerId().value());
        entity.setBusinessId(order.getBusinessId().value());
        entity.setPrice(order.getPrice().amount());
        entity.setTrackingId(order.getTrackingId().value());
        entity.setOrderStatus(order.getOrderStatus());
        entity.setFailureMessages(
                order.getFailureMessages() != null
                        ? new ArrayList<>(order.getFailureMessages())
                        : new ArrayList<>()
        );
        StreetAddress address = order.getDeliveryAddress();
        OrderAddressEntity addressEntity = new OrderAddressEntity();
        addressEntity.setId(address.id());
        addressEntity.setStreet(address.street());
        addressEntity.setPostalCode(address.postalCode());
        addressEntity.setCity(address.city());
        addressEntity.setOrder(entity);
        entity.setAddress(addressEntity);

        List<OrderItemEntity> itemEntities = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setId(item.getId().value());
            itemEntity.setOrder(entity);         // owning side of the relation
            itemEntity.setProductId(item.getProduct().getId().value());
            itemEntity.setProductName(item.getProduct().getName());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setPrice(item.getPrice().amount());
            itemEntity.setSubTotal(item.getSubTotal().amount());
            itemEntities.add(itemEntity);
        }
        entity.setItems(itemEntities);
        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        OrderId orderId = new OrderId(entity.getId());
        OrderAddressEntity addressEntity = entity.getAddress();

        List<OrderItem> items = entity.getItems().stream()
                .map(itemEntity -> OrderItem.Builder.newBuilder()
                        .id(new OrderItemId(itemEntity.getId()))
                        .orderId(orderId)
                        .product(new Product(
                                new ProductId(itemEntity.getProductId()),
                                itemEntity.getProductName(),
                                new Money(itemEntity.getPrice())))
                        .quantity(itemEntity.getQuantity())
                        .price(new Money(itemEntity.getPrice()))
                        .subTotal(new Money(itemEntity.getSubTotal()))
                        .build())
                .toList();

        return Order.Builder.newBuilder()
                .id(orderId)
                .customerId(new CustomerId(entity.getCustomerId()))
                .businessId(new BusinessId(entity.getBusinessId()))
                .deliveryAddress(new StreetAddress(addressEntity.getId(), addressEntity.getStreet(),
                        addressEntity.getPostalCode(), addressEntity.getCity()))
                .price(new Money(entity.getPrice()))
                .items(items)
                .trackingId(new TrackingId(entity.getTrackingId()))
                .orderStatus(entity.getOrderStatus())
                .failureMessages(entity.getFailureMessages())
                .build();
    }

}
