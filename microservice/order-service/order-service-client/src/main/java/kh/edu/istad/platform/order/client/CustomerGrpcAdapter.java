//package kh.edu.istad.platform.order.client;
//
//import com.example.customerservice.grpc.CustomerGetResponse;
//import com.example.customerservice.grpc.CustomerServiceGrpc;
//import com.example.customerservice.grpc.GetCustomerRequest;
//import io.grpc.StatusRuntimeException;
//import kh.edu.istad.common.domain.valueobject.CustomerId;
//import kh.edu.istad.platform.order.domain.dto.CustomerResult;
//import kh.edu.istad.platform.order.domain.entity.Customer;
//import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
//import net.devh.boot.grpc.client.inject.GrpcClient;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//
//@Service
//public class CustomerGrpcAdapter implements CustomerRepository {
//    @GrpcClient("customer-service")
//    private CustomerServiceGrpc.CustomerServiceBlockingStub customerServiceBlockingStub;
//    @Override
//    public Mono<Customer> findById(CustomerId customerId) {
//        return Mono.fromCallable(() -> {
//            GetCustomerRequest request = GetCustomerRequest.newBuilder()
//                    .setId(customerId.value().toString())
//                    .build();
//            CustomerGetResponse response = customerServiceBlockingStub.getCustomerById(request);
//            return new Customer(new CustomerId(UUID.fromString(response.getCustomerId())));
//        }).onErrorResume(StatusRuntimeException.class, ex -> Mono.empty());
//    }
//    @Override
//    public Flux<CustomerResult> getAllCustomer() {
//        return Flux.empty();
//    }
//}
