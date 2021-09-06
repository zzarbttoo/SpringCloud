package com.zzarbttoo.userservice.controller;

import com.netflix.discovery.converters.Auto;
import com.zzarbttoo.userservice.dto.UserDto;
import com.zzarbttoo.userservice.service.UserService;
import com.zzarbttoo.userservice.vo.Greeting;
import com.zzarbttoo.userservice.vo.RequestUser;
import com.zzarbttoo.userservice.vo.ResponseUser;
import org.dom4j.util.UserDataAttribute;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private Environment env;
    private UserService userService;


    @Autowired
    private Greeting greeting;


    @Autowired //bean이 자동 주입
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/health_check")
    public String status(){

        return "It's Working in User Service";
    }

    @GetMapping("/welcome")
    public String welcome(){
        //return env.getProperty("greeting.message");
        return greeting.getMessage();
    }


    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);

        userService.createUser(userDto);
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        //created = 201로 반환한다
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

}
