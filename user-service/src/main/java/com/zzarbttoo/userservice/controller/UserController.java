package com.zzarbttoo.userservice.controller;

import com.netflix.discovery.converters.Auto;
import com.zzarbttoo.userservice.vo.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    //Environment 객체는 @Autowired로 직접 주입하는 것이 비추됨 -> 생성자 생성
    private Environment env;

    @Autowired
    private Greeting greeting;

    // 생성자 생성 : (window -> alt, insert)
    @Autowired
    public UserController(Environment env) {
        this.env = env;
    }

    @GetMapping("/health_check")
    public String status(){
        return "It's working in user service";
    }

    @GetMapping("/welcome")
    public String welcome(){
        //return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

}
