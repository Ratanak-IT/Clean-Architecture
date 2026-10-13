package kh.edu.istad.platform.payment;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "kh.edu.istad.platform.payment",
        "kh.edu.istad.common.restapi"
})
public class PaymentApplicationService {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplicationService.class, args);
    }

}
