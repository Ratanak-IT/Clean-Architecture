package kh.edu.istad.platform.payment.restapi.mapper;

import kh.edu.istad.platform.payment.domain.dto.CancelPaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.CreditEntryResult;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.domain.dto.ProcessPaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.TopUpCreditCommand;
import kh.edu.istad.platform.payment.restapi.dto.CreditEntryResponse;
import kh.edu.istad.platform.payment.restapi.dto.CreditTopUpRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentCancelRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentProcessRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentWebMapper {

    public ProcessPaymentCommand toCommand(PaymentProcessRequest request) {
        if (request == null) return null;
        return new ProcessPaymentCommand(
                request.orderId(),
                request.customerId(),
                request.price()
        );
    }

    public CancelPaymentCommand toCommand(PaymentCancelRequest request) {
        if (request == null) return null;
        return new CancelPaymentCommand(
                request.paymentId(),
                request.orderId(),
                request.customerId(),
                request.price()
        );
    }

    public TopUpCreditCommand toCommand(CreditTopUpRequest request) {
        if (request == null) return null;
        return new TopUpCreditCommand(
                request.customerId(),
                request.amount()
        );
    }

    public PaymentResponse toResponse(PaymentResult result) {
        if (result == null) return null;
        return new PaymentResponse(
                result.paymentId(),
                result.customerId(),
                result.orderId(),
                result.price(),
                result.paymentStatus(),
                result.createdAt(),
                result.failureMessages()
        );
    }

    public CreditEntryResponse toResponse(CreditEntryResult result) {
        if (result == null) return null;
        return new CreditEntryResponse(
                result.creditEntryId(),
                result.customerId(),
                result.totalCreditAmount()
        );
    }
}
