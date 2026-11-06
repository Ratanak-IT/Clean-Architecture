package kh.edu.istad.platform.order.client.config;

import kh.edu.istad.platform.order.client.exception.DownStreamServiceException;
import kh.edu.istad.platform.order.client.httpclient.BusinessHttpClient;
import kh.edu.istad.platform.order.client.httpclient.CustomerHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.support.OAuth2WebClientHttpServiceGroupConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.reactive.function.client.support.WebClientHttpServiceGroupConfigurer;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.service.registry.HttpServiceGroup;
import org.springframework.web.service.registry.ImportHttpServices;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
@ImportHttpServices(
        group = "customer-service",
        types = {
        CustomerHttpClient.class
}, clientType = HttpServiceGroup.ClientType.WEB_CLIENT)
public class ClientConfig {

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService){
        return new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
    }

    @Bean
    public OAuth2WebClientHttpServiceGroupConfigurer oAuth2WebClientHttpServiceGroupConfigurer(
            ReactiveOAuth2AuthorizedClientManager authorizedClientManager){
        return OAuth2WebClientHttpServiceGroupConfigurer.from(authorizedClientManager);
    }


    @Bean
    public WebClientHttpServiceGroupConfigurer webClientHttpServiceGroupConfigurer(){
        return groups -> {
            groups.filterByName("customer-service")
                    .forEachClient((group , clientBuilder)->
                            clientBuilder.defaultHeader("User-Agent", "Web Client")
                                    .defaultStatusHandler(
                                            HttpStatusCode::isError,
                                            response -> {
                                                return response.createException()
                                                        .map(
                                                                ex ->
                                                                        new DownStreamServiceException(ex.getMessage(), "customer-service")
                                                        );
                                            }
                                    ));
        };

    }
//    private final CreateClient createClient;
//
//    @Bean
//    public CustomerHttpClient customerHttpClient(){
//        return createClient.createClient("http://customer-service", CustomerHttpClient.class);
//    }
//
//    @Bean
//    public BusinessHttpClient businessHttpClient(){
//        return createClient.createClient("http://business-service", BusinessHttpClient.class);
//    }
}
