//package kh.edu.istad.platform.order.persistence.adapter;
//
//import kh.edu.istad.common.domain.exception.ExternalServiceException;
//import kh.edu.istad.common.domain.valueobject.CustomerId;
//import kh.edu.istad.platform.order.domain.entity.Customer;
//import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
//import kh.edu.istad.platform.order.persistence.client.CustomerLookupResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import reactor.core.publisher.Mono;
//
//import java.util.Optional;
//
//@Repository
//public class CustomerRepositoryAdapter implements CustomerRepository {
//    private final WebClient customerWebClient;
//
//    public CustomerRepositoryAdapter(WebClient.Builder loadBalancedWebClientBuilder, @Value("${customer-service.base-url}") String baseUrl){
//        this.customerWebClient=loadBalancedWebClientBuilder.baseUrl(baseUrl).build();
//    }
//
//    @Override
//    public Mono<Customer> findById(CustomerId customerId) {
//            return customerWebClient
//                    .get()
//                    .uri("/api/customers/{customerId}", customerId.value())
//                    .retrieve()
//                    .bodyToMono(CustomerLookupResponse.class)
//                    .mapNotNull(response -> {
//                        if (response.customerId() == null){
//                            return null;
//                        }
//                        return new Customer(new CustomerId(response.customerId()));
//                    })
//                    .flatMap(Mono::just)
//                    .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
//                        return Mono.empty();
//                    })
//                    .onErrorResume(WebClientResponseException.BadRequest.class, ex -> {
//                        return Mono.empty();
//                    })
//                    .onErrorResume(WebClientResponseException.class, ex -> {
//                        return Mono.error(new ExternalServiceException(
//                                "Customer service error: " + ex.getStatusCode() + " " + ex.getResponseBodyAsString(), ex));
//                    });
//    }
//}
//
