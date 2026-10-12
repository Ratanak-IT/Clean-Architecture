package kh.edu.istad.platform.order.persistence.config;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CustomerClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient customerWebClient(WebClient.Builder builder, @Value("${customer-service.base-url}") String baseUrl) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }
}