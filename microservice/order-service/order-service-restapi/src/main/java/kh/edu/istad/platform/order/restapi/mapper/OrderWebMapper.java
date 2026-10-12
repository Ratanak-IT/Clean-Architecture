package kh.edu.istad.platform.order.restapi.mapper;

import kh.edu.istad.platform.order.domain.dto.AddressCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.dto.OrderItemCommand;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderWebMapper {

    public CreateOrderCommand toCommand(CreateOrderRequest request) {
        return new CreateOrderCommand(
                request.customerId(),
                request.businessId(),
                request.price(),
                request.items().stream()
                        .map(item -> new OrderItemCommand(
                                item.productId(), item.quantity(), item.price(), item.subTotal()))
                        .toList(),
                new AddressCommand(
                        request.address().street(),
                        request.address().postalCode(),
                        request.address().city()));
    }

    public CreateOrderResponse toResponse(CreateOrderResult result) {
        return new CreateOrderResponse(result.trackingId(), result.orderStatus(), result.message());
    }
}
