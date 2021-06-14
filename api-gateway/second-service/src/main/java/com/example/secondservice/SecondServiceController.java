package com.example.secondservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//http://localhost:8081/welcome
//http://localhost:8081/first-service/welcome

@Slf4j
@RestController
@RequestMapping("/second-service")
public class SecondServiceController {

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the Second Service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header){
        log.info(header);
        return "Hello world in Second Service";

    }

    @GetMapping("/check")
    public String check(){
        return "Hi, there This is a message from Second Service";
    }
}
