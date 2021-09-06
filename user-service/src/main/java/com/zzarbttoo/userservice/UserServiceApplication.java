package com.zzarbttoo.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    //초기화를 위해 인자의 bean도 초기화되어 메모리에 적재되어야 한다
    //하지만 선언할 곳이 없다(legacy에서는 xml로 bean 등록)
    // springboot에서는 가장 먼저 실행되는 springbootApplication에 Bean 등록을 해줘야한다

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
