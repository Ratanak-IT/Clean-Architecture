package kh.edu.istad.platform.order.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "service")
@Getter
@Setter
public class ServicePropConfig {
    private String message;
    private String version;
    private String env;
    private String baseUrl;
}