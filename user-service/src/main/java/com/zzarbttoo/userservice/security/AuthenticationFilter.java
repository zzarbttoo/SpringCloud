package com.zzarbttoo.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzarbttoo.userservice.dto.UserDto;
import com.zzarbttoo.userservice.service.UserService;
import com.zzarbttoo.userservice.vo.RequestLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //생성하는 곳에서 직접 userService를 생성해서 쓰기 때문에 주입하지 않아도 된다
    private UserService userService;
    private Environment env; //토큰 만료 기간, 토큰 생성 알고리즘 등을 application.yml에 작성할 것이다

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService,
                                Environment env) {
        super.setAuthenticationManager(authenticationManager); // super(authenticationManger);
        this.userService = userService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {

            //post형태로 전달되는 것은 requestParameter로 받을 수 없기 때문에 inputstream으로 처리
            RequestLogin credential = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            //인증 정보를 매니저에 넘긴다(id, password 비교)
            return getAuthenticationManager().authenticate(
            //인증정보로 만든다
            new UsernamePasswordAuthenticationToken(credential.getEmail(),
                    credential.getPassword(),
                    new ArrayList<>())
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //로그인 성공 시 정확하게 어떤 처리를 해줄 것인지(ex Token 생성/만료 시간 등, 사용자 반환값 등)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        //User객체는 login 성공 후 email과 encrypted 된 password, 권한들을 가진 객체
        //log.debug(((User) authResult.getPrincipal()).getUsername()); //성공한 email 출력
        String userName = ((User) authResult.getPrincipal()).getUsername();

        UserDto userDetails = userService.getUserDetailsByEmail(userName);

        //JWT token
        String token = Jwts.builder()
                .setSubject(userDetails.getUserId()) //userId로 토큰 생성
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time")))) //현재 시간 + 24시간
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret")) //알고리즘 + key 값
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());

    }
}
