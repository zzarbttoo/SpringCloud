package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        /*return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage: -> {}", config.getBaseMessage());


            if(config.isPreLogger()){
                log.info("Global Filter Start: request id -> {}", request.getId());
            }
            //custom Post Filter(chain)
            //비동기 방식에서 사용 -> Mono (단일값)
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
            {

                if(config.isPostLogger()){
                    log.info("Global Filter end: response code -> {}", response.getStatusCode());
                }
            }));
        };
         */
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage: -> {}", config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("Logging Pre Filter: request id -> {}", request.getId());
            }
            //custom Post Filter(chain)
            //비동기 방식에서 사용 -> Mono (단일값)
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
            {

                if(config.isPostLogger()){
                    log.info("Logging Post Filter: response code -> {}", response.getStatusCode());
                }
            }));

        }, Ordered.LOWEST_PRECEDENCE); //filter의 order 지정
        //우선 순위를 가장 높게 잡았기 때문에 가장 먼저 실행 (Highest_Precedence)



        return filter;
    }

    @Data //getter setter
    public static class Config{
        //Put the configuration properties
        private String baseMessage;
        private boolean preLogger;  // boolean은 is~ 로 자동 생성
        private boolean postLogger;

    }
}
