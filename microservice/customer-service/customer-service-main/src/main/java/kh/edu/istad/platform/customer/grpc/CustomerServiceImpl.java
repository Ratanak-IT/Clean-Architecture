package kh.edu.istad.platform.customer.grpc;

import com.example.customerservice.grpc.CustomerGetResponse;
import com.example.customerservice.grpc.CustomerServiceGrpc;
import com.example.customerservice.grpc.GetCustomerRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.port.out.CustomerMonoRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {
    private final CustomerMonoRepository customerMonoRepository;
    @Override
    public void getCustomerById(GetCustomerRequest request, StreamObserver<CustomerGetResponse> responseObserver) {
        CustomerId customerId = new CustomerId(UUID.fromString(request.getId()));
        customerMonoRepository.findById(customerId)
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
                                        .asRuntimeException()));}}
