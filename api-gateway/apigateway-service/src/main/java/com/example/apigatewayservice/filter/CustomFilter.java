package com.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest; //webflux
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){

        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        // Custom Pre Filter
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom Pre filter : request id -> {}", request.getId());


            //custom Post Filter(chain)
            //비동기 방식에서 사용 -> Mono (단일값)
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
            {
                log.info("Custom Post filter : response code -> {}", response.getStatusCode());
            }));
        };
    }

    public static class Config{
        //Put the configuration properties

    }
}
