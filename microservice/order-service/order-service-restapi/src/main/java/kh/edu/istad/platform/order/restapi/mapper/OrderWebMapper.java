package kh.edu.istad.platform.order.restapi.mapper;

import kh.edu.istad.platform.order.domain.dto.AddressCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.dto.OrderItemCommand;
import kh.edu.istad.platform.order.domain.dto.PayOrderResult;
import kh.edu.istad.platform.order.domain.dto.TrackOrderResult;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderResponse;
import kh.edu.istad.platform.order.restapi.dto.PayOrderResponse;
import kh.edu.istad.platform.order.restapi.dto.TrackOrderResponse;
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

    public TrackOrderResponse toResponse(TrackOrderResult result) {
        if (result == null) return null;
        return new TrackOrderResponse(
                result.orderId(),
                result.trackingId(),
                result.customerId(),
                result.orderStatus(),
                result.price(),
                result.failureMessages()
        );
    }

    public PayOrderResponse toResponse(PayOrderResult result) {
        if (result == null) return null;
        return new PayOrderResponse(
                result.orderId(),
                result.trackingId(),
                result.orderStatus(),
                result.message()
        );
    }
}


