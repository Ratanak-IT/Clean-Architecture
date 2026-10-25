package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.order.persistence.client.BusinessLookupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

@Repository
public class BusinessRepositoryAdapter implements BusinessRepository {
    private final WebClient businessWebClient;

    public BusinessRepositoryAdapter(WebClient.Builder loadBalanceWebClientBuinder, @Value("${business-service.base-url}") String baseUrl) {
        this.businessWebClient = loadBalanceWebClientBuinder.baseUrl(baseUrl).build();
    }
    @Override
    public Optional<Business> findById(BusinessId businessId) {
        try {
            BusinessLookupResponse response = businessWebClient
                    .get()
                    .uri("/api/businesses/{businessId}", businessId.value())
                    .retrieve()
                    .bodyToMono(BusinessLookupResponse.class)
                    .block();

            if (response == null || response.businessId() == null) {
                return Optional.empty();
            }

            List<Product> products = response.products() == null
                    ? List.of()
                    : response.products().stream()
                    .map(p -> new Product(
                            new ProductId(p.productId()),
                            p.name(),
                            new Money(p.price())))
                    .toList();

            return Optional.of(new Business(
                    new BusinessId(response.businessId()),
                    products,
                    response.active()));

        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        }
    }
}