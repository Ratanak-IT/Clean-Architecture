package kh.edu.istad.platform.order.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.order.domain.usecase.CreateOrderUseCase;
import kh.edu.istad.platform.order.domain.usecase.PayOrderUseCase;
import kh.edu.istad.platform.order.domain.usecase.TrackOrderUseCase;
import kh.edu.istad.platform.order.restapi.dto.OrderCreateRequest;
import kh.edu.istad.platform.order.restapi.dto.OrderCreateResponse;
import kh.edu.istad.platform.order.restapi.dto.OrderPayResponse;
import kh.edu.istad.platform.order.restapi.dto.OrderTrackResponse;
import kh.edu.istad.platform.order.restapi.mapper.OrderWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final TrackOrderUseCase trackOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final OrderWebMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderCreateResponse createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return mapper.toResponse(createOrderUseCase.execute(mapper.toCommand(request)));
    }

//    @PostMapping("/{orderId}/pay")
//    @ResponseStatus(HttpStatus.OK)
//    public OrderPayResponse payOrder(@PathVariable("orderId") UUID orderId) {
//        return mapper.toResponse(payOrderUseCase.execute(orderId));
//    }

    @PatchMapping("/{orderId}/pay")
    @ResponseStatus(HttpStatus.OK)
    public OrderPayResponse patchPayOrder(@PathVariable("orderId") UUID orderId) {
        return mapper.toResponse(payOrderUseCase.execute(orderId));
    }

    @GetMapping("/{trackingId}")
    public OrderTrackResponse trackOrder(@PathVariable("trackingId") UUID trackingId) {
        return mapper.toResponse(trackOrderUseCase.execute(trackingId));
    }
}


