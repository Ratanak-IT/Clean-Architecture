package kh.edu.istad.platform.customer.grpc;


import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.customerservice.grpc.CustomerServiceGrpc;
import kh.edu.istad.platform.customer.domain.port.out.CustomerMonoRepository;
import kh.edu.istad.customerservice.grpc.CustomerGetResponse;
import kh.edu.istad.customerservice.grpc.GetAllCustomerRequest;
import kh.edu.istad.customerservice.grpc.GetCustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;


import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {
    private final CustomerMonoRepository customerMonoRepository;
    @Override
    public void getCustomerById(GetCustomerRequest request, StreamObserver<CustomerGetResponse> responseObserver) {
        CustomerId customerId = new CustomerId(UUID.fromString(request.getId()));
        customerMonoRepository.findById(customerId)
                .switchIfEmpty(reactor.core.publisher.Mono.error(
                        new ResourceNotFoundException("Customer", customerId.value())))
                .map(result -> CustomerGetResponse.newBuilder()
                        .setCustomerId(result.customerId())
                        .setUsername(result.username())
                        .setFamilyName(result.familyName())
                        .setGivenName(result.givenName())
                        .setEmail(result.email())
                        .setPhoneNumber(result.phoneNumber())
                        .setCustomerStatus(result.customerStatus().toString())
                        .setSegment(result.segment() != null ? result.segment().toString() : "").build())
                .subscribe(
                        response -> {
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        }, error -> responseObserver.onError(
                                Status.NOT_FOUND
                                        .withDescription(error.getMessage())
                                        .asRuntimeException()));
    }

    @Override
    public void getAllCustomer(GetAllCustomerRequest request, StreamObserver<CustomerGetResponse> responseObserver) {
       customerMonoRepository.findAll()
               .map(customer -> CustomerGetResponse.newBuilder()
                       .setCustomerId(customer.customerId())
                       .setUsername(customer.username())
                       .setFamilyName(customer.familyName())
                       .setGivenName(customer.givenName())
                       .setEmail(customer.email())
                       .setPhoneNumber(customer.phoneNumber())
                       .setCustomerStatus(customer.customerStatus().name())
                       .setSegment(customer.segment() != null ? customer.segment().toString() : "")
                       .build())
               .subscribe(
                       responseObserver::onNext, //sent data to client as stream
                       responseObserver::onError, // if you have error
                       responseObserver::onCompleted  // end the steam when sent all data
               );
    }
}
