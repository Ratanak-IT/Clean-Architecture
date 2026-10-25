package kh.edu.istad.platform.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class BusinessApplicationService {
    public static void main(String[] args) {
        SpringApplication.run(BusinessApplicationService.class, args);
    }
}
