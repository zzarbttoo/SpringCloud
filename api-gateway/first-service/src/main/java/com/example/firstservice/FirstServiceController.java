package com.example.firstservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/first-service")
public class FirstServiceController {

    Environment env; // 현재 포트 번호를 알기 위해서 Environment 객체 사용

    @Autowired //직접 주입 받는 것이 아니라 생성자 통해 주입 받도록 권장
    public FirstServiceController(Environment env){
        this.env = env;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the First Service";
    }

    //header(first-request)를 받아서 header에 저장
    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header){
        log.info(header);
        return "Hello world in First Service";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        log.info("Server port = {}", request.getServerPort()); //HttpServletRequest는 선언만 하고 바로 사용 가능

        return String.format("Hi there this is message from First Service on Port %s", env.getProperty("local.server.port"));
        //원래는 yml 파일에 명시된 값을 env.getProperty("") 안에 적어주면 된다
        //하지만 지금은 할당된 random port 값을 가져오려고 하기 때문에 local.server.port를 적어 줄 것이다
    }
}
