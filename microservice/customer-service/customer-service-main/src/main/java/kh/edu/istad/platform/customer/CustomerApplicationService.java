package kh.edu.istad.platform.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {
        "kh.edu.istad.platform.customer",
        "kh.edu.istad.common.restapi"
})
@EnableDiscoveryClient
public class CustomerApplicationService {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplicationService.class, args);
    }
}