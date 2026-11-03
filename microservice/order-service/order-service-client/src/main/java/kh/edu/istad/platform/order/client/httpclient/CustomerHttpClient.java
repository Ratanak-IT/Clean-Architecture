package kh.edu.istad.platform.order.client.httpclient;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.domain.dto.CustomerResult;
import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@HttpExchange("/api/customers")
public interface CustomerHttpClient {

    @GetExchange
    @ClientRegistrationId("customer-client")
    public Flux<CustomerResult> getAllCustomer();

    @GetExchange("/{id}")
    @ClientRegistrationId("customer-client")
    public Mono<CustomerResult> getCustomerById(@PathVariable CustomerId id);
}