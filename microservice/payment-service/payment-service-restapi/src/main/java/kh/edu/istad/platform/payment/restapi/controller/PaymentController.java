package kh.edu.istad.platform.payment.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.payment.domain.dto.CancelPaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.CreditEntryResult;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.domain.dto.ProcessPaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.TopUpCreditCommand;
import kh.edu.istad.platform.payment.domain.usecase.CancelPaymentUseCase;
import kh.edu.istad.platform.payment.domain.usecase.GetCreditEntryByCustomerIdUseCase;
import kh.edu.istad.platform.payment.domain.usecase.GetPaymentByIdUseCase;
import kh.edu.istad.platform.payment.domain.usecase.GetPaymentByOrderIdUseCase;
import kh.edu.istad.platform.payment.domain.usecase.ProcessPaymentUseCase;
import kh.edu.istad.platform.payment.domain.usecase.TopUpCreditUseCase;
import kh.edu.istad.platform.payment.restapi.dto.CreditEntryResponse;
import kh.edu.istad.platform.payment.restapi.dto.CreditTopUpRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentCancelRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentProcessRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentResponse;
import kh.edu.istad.platform.payment.restapi.mapper.PaymentWebMapper;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final CancelPaymentUseCase cancelPaymentUseCase;
    private final GetPaymentByIdUseCase getPaymentByIdUseCase;
    private final GetPaymentByOrderIdUseCase getPaymentByOrderIdUseCase;
    private final GetCreditEntryByCustomerIdUseCase getCreditEntryByCustomerIdUseCase;
    private final TopUpCreditUseCase topUpCreditUseCase;
    private final PaymentWebMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse processPayment(@Valid @RequestBody PaymentProcessRequest request) {
        ProcessPaymentCommand command = mapper.toCommand(request);
        PaymentResult result = processPaymentUseCase.execute(command);
        return mapper.toResponse(result);
    }

    @PostMapping("/credit")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditEntryResponse topUpCredit(@Valid @RequestBody CreditTopUpRequest request) {
        TopUpCreditCommand command = mapper.toCommand(request);
        CreditEntryResult result = topUpCreditUseCase.execute(command);
        return mapper.toResponse(result);
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse cancelPayment(@Valid @RequestBody PaymentCancelRequest request) {
        CancelPaymentCommand command = mapper.toCommand(request);
        PaymentResult result = cancelPaymentUseCase.execute(command);
        return mapper.toResponse(result);
    }

    @PatchMapping("/{paymentId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse cancelPaymentById(@PathVariable("paymentId") UUID paymentId) {
        CancelPaymentCommand command = new CancelPaymentCommand(paymentId, null, null, null);
        PaymentResult result = cancelPaymentUseCase.execute(command);
        return mapper.toResponse(result);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getPaymentById(@PathVariable("paymentId") UUID paymentId) {
        PaymentResult result = getPaymentByIdUseCase.execute(paymentId);
        return mapper.toResponse(result);
    }

    @GetMapping("/order/{orderId}")
    public PaymentResponse getPaymentByOrderId(@PathVariable("orderId") UUID orderId) {
        PaymentResult result = getPaymentByOrderIdUseCase.execute(orderId);
        return mapper.toResponse(result);
    }

    @GetMapping("/customer/{customerId}/credit")
    public CreditEntryResponse getCreditByCustomerId(@PathVariable("customerId") UUID customerId) {
        CreditEntryResult result = getCreditEntryByCustomerIdUseCase.execute(customerId);
        return mapper.toResponse(result);
    }
}
