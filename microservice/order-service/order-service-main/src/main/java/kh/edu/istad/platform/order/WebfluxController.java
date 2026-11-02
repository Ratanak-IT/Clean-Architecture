package kh.edu.istad.platform.order;

import kh.edu.istad.platform.order.config.ServicePropConfig;
import kh.edu.istad.platform.order.domain.dto.CustomerResult;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RefreshScope
@RequiredArgsConstructor
public class WebfluxController {
    private final CustomerRepository customerRepository;
    private final ServicePropConfig servicePropConfig;

    @GetMapping("/customers")
    public Flux<CustomerResult> getCustomers(){
        return customerRepository.getAllCustomer();
    }



    @Value("${globle.version}")
    private String consulVer;


    @GetMapping("/consul/config")
    public Mono<Map<String, Object>> getConsulConfig(){
        return Mono.just(Map.of(
                "version", consulVer,
                "consulMessage", servicePropConfig.getMessage(),
                "consulVersion", servicePropConfig.getVersion(),
                "consulEnv", servicePropConfig.getEnv(),
                "baseUrl", servicePropConfig.getBaseUrl()
        ));
    }
}
