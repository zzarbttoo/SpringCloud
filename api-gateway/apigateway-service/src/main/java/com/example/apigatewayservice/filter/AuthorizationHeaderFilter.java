package com.example.apigatewayservice.filter;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);  //없으면 class cast exception 발생
        this.env = env;
    }

    //login -> token -> users(with token) -> header(include token)
    //Token의 유효성을 확인한 후 통과시킴
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();


            //인증정보가 포함되어있지 않다면 통과시키지 않는다
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
            }

            //배열이기 때문에 0번째 data를 들고온다 -> authorization 에는 Bearer Token 값이 들어가게 된다
            String authorizationHeader = request.getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);

            // Beraer token 형태로 오기 때문에 Bearer을 ""로 변환하고 순수 token 값을 얻어낸다
            String jwt = authorizationHeader.replace("Bearer", "");

            //JWT가 정상적인 값일 경우 통과
            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);

        });
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;

        try {
            //subject(sub)를 token으로부터 추출한다 -> 정상적인 계정 값인지 판별
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
                    //.parseClaimsJws(jwt).getBody().getSubject();
        } catch (Exception ex){
        returnValue = false;
        }

        //TODO : user_id 값도 비교하는 로직 만들기
        if(subject == null || subject.isEmpty()){
            returnValue = false;
        }

        return returnValue;

}

    //(반환시켜주는 데이터 타입) Mono(단일), Flux(여러개) -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(error);
        return response.setComplete();
    }

    public static class Config{

    }

}


