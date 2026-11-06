package kh.edu.istad.platform.order;

import build.buf.gen.customer.CustomerServiceGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.client.ImportGrpcClients;

//@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
        "kh.edu.istad.platform.order",
        "kh.edu.istad.common.restapi"
})
@ImportGrpcClients(
        basePackageClasses = CustomerServiceGrpc.class,
        target = "customer-channel"
)
public class OrderApplicationService {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplicationService.class, args);
    }
}
