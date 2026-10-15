package kh.edu.istad.platform.order.restapi.mapper;

import kh.edu.istad.platform.order.domain.dto.AddressCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.dto.OrderItemCommand;
import kh.edu.istad.platform.order.domain.dto.PayOrderResult;
import kh.edu.istad.platform.order.domain.dto.TrackOrderResult;
import kh.edu.istad.platform.order.restapi.dto.OrderCreateRequest;
import kh.edu.istad.platform.order.restapi.dto.OrderCreateResponse;
import kh.edu.istad.platform.order.restapi.dto.OrderPayResponse;
import kh.edu.istad.platform.order.restapi.dto.OrderTrackResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderWebMapper {

    public CreateOrderCommand toCommand(OrderCreateRequest request) {
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

    public OrderCreateResponse toResponse(CreateOrderResult result) {
        return new OrderCreateResponse(result.trackingId(), result.orderStatus(), result.message());
    }

    public OrderTrackResponse toResponse(TrackOrderResult result) {
        if (result == null) return null;
        return new OrderTrackResponse(
                result.orderId(),
                result.trackingId(),
                result.customerId(),
                result.orderStatus(),
                result.price(),
                result.failureMessages()
        );
    }

    public OrderPayResponse toResponse(PayOrderResult result) {
        if (result == null) return null;
        return new OrderPayResponse(
                result.orderId(),
                result.trackingId(),
                result.orderStatus(),
                result.message()
        );
    }
}


