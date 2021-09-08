package com.zzarbttoo.userservice.security;

import com.zzarbttoo.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

@Configuration //configuration은 우선순위가 높다
@EnableWebSecurity //webSecurity로 등록한다는 얘기
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Environment env;
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.env = env; //토큰 시간 등을 저장하기 위해 env 사용
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();


        //인증이 된 상태에서만 permit
        http.authorizeRequests().antMatchers("/**")
                .hasIpAddress("192.168.0.20") //특정 ip에 대해서만 permit
                .and()
                .addFilter(getAuthenticationFilter()); //filter을 통과해야만 permit

        http.headers().frameOptions().disable();

    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, env);
        //authenticationFilter.setAuthenticationManager(authenticationManager()); //생성자를 통해 만들어서 의미가 없다
        //authenticationManager에 spring security의 login 기능 등록

        return authenticationFilter;

    }

    //인증과 관련된 configure
    //select pwd from user where email = ?
    // db_pwd(encrypted) == input_pwd(encrypted)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder); //select는 userService
        //변환처리는 이전에 등록해놓은 bCryptPasswordEncoder bean 이용


        super.configure(auth);
    }
}
