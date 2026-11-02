package kh.edu.istad.platform.order.client.httpclient;


import kh.edu.istad.platform.order.domain.dto.BusinessResult;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Flux;

@HttpExchange
public interface BusinessHttpClient {

    public Flux<BusinessResult> getAllBusiness();
}
