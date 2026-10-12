package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.domain.entity.Customer;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.order.persistence.client.CustomerLookupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final WebClient customerWebClient;

    @Override
    public Optional<Customer> findById(CustomerId customerId) {
        try {
            CustomerLookupResponse response = customerWebClient
                    .get()
                    .uri("/api/customers/{customerId}", customerId.value())
                    .retrieve()
                    .bodyToMono(CustomerLookupResponse.class)
                    .block();

            if (response == null || response.customerId() == null) {
                return Optional.empty();
            }

            return Optional.of(
                    new Customer(new CustomerId(response.customerId()))
            );

        } catch (WebClientResponseException.NotFound exception) {
            return Optional.empty();
        }
    }
}
