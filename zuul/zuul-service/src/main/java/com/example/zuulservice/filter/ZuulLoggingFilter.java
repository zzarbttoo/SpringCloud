package com.example.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Component
public class ZuulLoggingFilter extends ZuulFilter {

    //log, 인증정보, 변환작업 등을 여기서 처리할 수 있다
    @Override
    public Object run() throws ZuulException {

        log.info("---------------printing logs: ");

        //web 프로젝트에 request/response 정보를 가지고 있는 최상위 객체
        RequestContext ctx= RequestContext.getCurrentContext();
        //사용자 request 정보 출력
        HttpServletRequest request = ctx.getRequest();

        log.info("---------------------------: " + request.getRequestURI());
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    //순서
    @Override
    public int filterOrder() {
        return 1;
    }

    //필터로 쓸거니까 true
    @Override
    public boolean shouldFilter() {
        return true;
    }


}
