package kh.edu.istad.platform.order.client.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class CreateClient {
    private final WebClient.Builder webClient;

    public <T> T createClient(String url, Class<T> type){
        WebClient client = webClient
                .baseUrl(url)
                .build();
        return createClient(client,type);
    }

    private <T> T createClient(WebClient wc, Class<T> type){

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(wc))
                .build();
        return factory.createClient(type);
    }

}
