package kh.edu.istad.platform.order.client;

import io.grpc.StatusRuntimeException;
import io.grpc.Status;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.customerservice.grpc.CustomerGetResponse;
import kh.edu.istad.customerservice.grpc.CustomerServiceGrpc;
import kh.edu.istad.customerservice.grpc.GetCustomerRequest;
import kh.edu.istad.customerservice.grpc.GetAllCustomerRequest;
import kh.edu.istad.platform.order.domain.dto.CustomerResult;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.order.client.exception.DownStreamServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CustomerGrpcAdapter implements CustomerRepository {

    private final CustomerServiceGrpc.CustomerServiceBlockingStub customerServiceBlockingStub;
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    @Override
    public Mono<CustomerResult> findById(CustomerId customerId) {
        return Mono.fromCallable(() -> {
            GetCustomerRequest request = GetCustomerRequest.newBuilder()
                    .setId(customerId.value().toString())
                    .build();
            var authorizedClient = authorizedClientManager.authorize(
                    OAuth2AuthorizeRequest.withClientRegistrationId("customer-client")
                            .principal("order-service")
                            .build()
            ).block();
            if (authorizedClient == null) {
                throw new IllegalStateException("Could not obtain OAuth2 access token for customer-service");
            }
            Metadata headers = new Metadata();
            headers.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER),
                    "Bearer " + authorizedClient.getAccessToken().getTokenValue());
            CustomerGetResponse response = customerServiceBlockingStub
                    .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(headers))
                    .getCustomerById(request);
            return new CustomerResult(response.getCustomerId(), response.getUsername(), response.getFamilyName(),response.getGivenName()
            ,response.getEmail(),response.getPhoneNumber(),response.getCustomerStatus(), response.getSegment());
        })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(StatusRuntimeException.class, ex -> {
                    if (ex.getStatus().getCode() == Status.Code.NOT_FOUND) {
                        return new ResourceNotFoundException(ex.getStatus().getDescription() != null
                                ? ex.getStatus().getDescription()
                                : "Customer with id " + customerId.value() + " not found");
                    }
                    return new DownStreamServiceException(ex.getMessage(), "customer-service");
                });
    }
    
    @Override
    public Flux<CustomerResult> getAllCustomer() {
        return Flux.defer(() -> {
            var authorizedClient = authorizedClientManager.authorize(
                    OAuth2AuthorizeRequest.withClientRegistrationId("customer-client")
                            .principal("order-service")
                            .build()
            ).block();
            if (authorizedClient == null) {
                return Flux.error(new IllegalStateException(
                        "Could not obtain OAuth2 access token for customer-service"));
            }

            Metadata headers = new Metadata();
            headers.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER),
                    "Bearer " + authorizedClient.getAccessToken().getTokenValue());
            var responses = customerServiceBlockingStub
                    .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(headers))
                    .getAllCustomer(GetAllCustomerRequest.getDefaultInstance());
            Iterable<CustomerGetResponse> responseIterable = () -> responses;

            return Flux.fromIterable(responseIterable)
                    .map(response -> new CustomerResult(
                            response.getCustomerId(),
                            response.getUsername(),
                            response.getFamilyName(),
                            response.getGivenName(),
                            response.getEmail(),
                            response.getPhoneNumber(),
                            response.getCustomerStatus(),
                            response.getSegment()));
        })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(StatusRuntimeException.class,
                        ex -> new DownStreamServiceException(ex.getMessage(), "customer-service"));
    }
}
