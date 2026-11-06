package kh.edu.istad.platform.order;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.config.ServicePropConfig;
import kh.edu.istad.platform.order.domain.dto.CustomerResult;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

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

    @GetMapping("/customers/{id}")
    public Mono<CustomerResult> getById(@PathVariable UUID id){
        return customerRepository.findById(new CustomerId(id));
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
