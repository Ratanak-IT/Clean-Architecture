package kh.edu.istad.platform.order.domain.mapper;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.common.domain.valueobject.StreetAddress;
import kh.edu.istad.platform.order.domain.dto.AddressCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.dto.OrderItemCommand;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateOrderMapper {

    public Order toOrder(CreateOrderCommand command) {
        AddressCommand address = command.address();
        return Order.Builder.newBuilder()
                .customerId(new CustomerId(command.customerId()))
                .businessId(new BusinessId(command.businessId()))
                .deliveryAddress(StreetAddress.of(address.street(), address.postalCode(), address.city()))
                .price(new Money(command.price()))
                .items(toOrderItems(command.items()))
                .build();
    }

    private List<OrderItem> toOrderItems(List<OrderItemCommand> items) {
        return items.stream()
                .map(item -> OrderItem.Builder.newBuilder()
                        .product(new Product(new ProductId(item.productId())))
                        .quantity(item.quantity())
                        .price(new Money(item.price()))
                        .subTotal(new Money(item.subTotal()))
                        .build())
                .toList();
    }

    public CreateOrderResult toResult(Order order, String message) {
        return new CreateOrderResult(order.getTrackingId().value(), order.getOrderStatus(), message);
    }
}
