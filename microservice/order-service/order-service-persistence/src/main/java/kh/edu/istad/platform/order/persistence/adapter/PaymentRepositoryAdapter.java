package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.platform.order.domain.dto.PaymentResponseDto;
import kh.edu.istad.platform.order.domain.port.out.PaymentRepository;
import kh.edu.istad.platform.order.persistence.client.PaymentLookupResponse;
import kh.edu.istad.platform.order.persistence.client.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Repository
@Slf4j
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final WebClient paymentWebClient;

    public PaymentRepositoryAdapter(WebClient.Builder loadBalancedWebClientBuilder, @Value("${payment-service.base-url}") String baseUrl){
        this.paymentWebClient=loadBalancedWebClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public PaymentResponseDto processPayment(OrderId orderId, CustomerId customerId, Money price) {
        PaymentRequest request = new PaymentRequest(
                orderId.value(),
                customerId.value(),
                price.amount()
        );

        log.info("Sending payment request to payment-service for orderId: {}, customerId: {}, price: {}",
                orderId.value(), customerId.value(), price.amount());

        try {
            PaymentLookupResponse response = paymentWebClient
                    .post()
                    .uri("/api/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PaymentLookupResponse.class)
                    .block();

            if (response == null) {
                log.error("Received null response from payment-service for orderId: {}", orderId.value());
                return new PaymentResponseDto(
                        null,
                        customerId.value(),
                        orderId.value(),
                        price.amount(),
                        PaymentStatus.FAILED,
                        List.of("Null response from payment service")
                );
            }

            log.info("Payment response received for orderId: {}, status: {}", orderId.value(), response.paymentStatus());
            return new PaymentResponseDto(
                    response.paymentId(),
                    response.customerId(),
                    response.orderId(),
                    response.price(),
                    response.paymentStatus(),
                    response.failureMessages()
            );

        } catch (WebClientResponseException e) {
            log.error("HTTP error from payment-service: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return new PaymentResponseDto(
                    null,
                    customerId.value(),
                    orderId.value(),
                    price.amount(),
                    PaymentStatus.FAILED,
                    List.of("Payment service error: " + e.getStatusCode() + " " + e.getResponseBodyAsString())
            );
        } catch (Exception e) {
            log.error("Connection error to payment-service: {}", e.getMessage());
            return new PaymentResponseDto(
                    null,
                    customerId.value(),
                    orderId.value(),
                    price.amount(),
                    PaymentStatus.FAILED,
                    List.of("Unable to connect to payment service: " + e.getMessage())
            );
        }
    }
}
