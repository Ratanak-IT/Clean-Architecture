//package kh.edu.istad.platform.order;
//
//
//import kh.edu.istad.common.domain.exception.ExternalServiceException;
//import kh.edu.istad.common.domain.valueobject.CustomerId;
//import kh.edu.istad.platform.order.client.httpclient.CustomerHttpClient;
//import kh.edu.istad.platform.order.dto.CustomerLookupResponse;
//import kh.edu.istad.platform.order.dto.CustomerResponse;
//import kh.edu.istad.platform.order.domain.dto.CustomerResult;
//import kh.edu.istad.platform.order.domain.entity.Customer;
//import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Repository
//public class CustomerAdapter implements CustomerRepository {
////    private final WebClient customerWebClient;
//    private final CustomerHttpClient customerHttpClient;
//
//
//    public CustomerAdapter(WebClient.Builder loadBalancedWebClientBuilder, @Value("${customer-service.base-url}") String baseUrl, CustomerHttpClient customerHttpClient){
//        this.customerHttpClient = customerHttpClient;
////        this.customerWebClient=loadBalancedWebClientBuilder.baseUrl(baseUrl).build();
//    }
//
//    @Override
//    public Mono<CustomerResult> findById(CustomerId customerId) {
////        return customerWebClient
////                .get()
////                .uri("/api/customers/{customerId}", customerId.value())
////                .retrieve()
////                .bodyToMono(CustomerLookupResponse.class)
////                .mapNotNull(response -> {
////                    if (response.customerId() == null){
////                        return null;
////                    }
////                    return new Customer(new CustomerId(response.customerId()));
////                })
////                .flatMap(Mono::just)
////                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
////                    return Mono.empty();
////                })
////                .onErrorResume(WebClientResponseException.BadRequest.class, ex -> {
////                    return Mono.empty();
////                })
////                .onErrorResume(WebClientResponseException.class, ex -> {
////                    return Mono.error(new ExternalServiceException(
////                            "Customer service error: " + ex.getStatusCode() + " " + ex.getResponseBodyAsString(), ex));
////                });
//        return customerHttpClient.getCustomerById(customerId);
//    }
//
//    @Override
//    public Flux<CustomerResult> getAllCustomer() {
////        return customerWebClient
////                .get()
////                .uri("/api/customers")
////                .retrieve()
////                .bodyToFlux(CustomerResponse.class)
////                .map(response -> new CustomerResult(
////                        response.customerId(),
////                        response.username(),
////                        response.familyName(),
////                        response.givenName(),
////                        response.email(),
////                        response.phoneNumber(),
////                        response.customerStatus(),
////                        response.segment()
////                ))
////                .onErrorResume(WebClientResponseException.class, ex -> {
////                    return Flux.error(new ExternalServiceException(
////                            "Failed to fetch customers: " + ex.getStatusCode() + " " + ex.getResponseBodyAsString(), ex));
////                });
//        return customerHttpClient.getAllCustomer();
//    }
//}