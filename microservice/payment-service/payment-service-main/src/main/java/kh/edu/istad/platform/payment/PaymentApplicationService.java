package kh.edu.istad.platform.payment;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {
        "kh.edu.istad.platform.payment",
        "kh.edu.istad.common.restapi"
})
@EnableDiscoveryClient
public class PaymentApplicationService {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplicationService.class, args);
    }

}
