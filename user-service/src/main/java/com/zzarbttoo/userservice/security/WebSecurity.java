package com.zzarbttoo.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration //configuration은 우선순위가 높다
@EnableWebSecurity //webSecurity로 등록한다는 얘기
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // /users/로 시작하는 모든 것은 permit
        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.headers().frameOptions().disable(); //h2가 frame별로 구분되어있기 때문에 이것을 설정하지 않으면 에러 발생
    }
}
