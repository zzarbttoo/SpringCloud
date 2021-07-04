package com.zzarbttoo.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor // lombok 중 argument를 다 가진 생성자
@NoArgsConstructor // lombok 중 argument가 없는 생성자
public class Greeting {

    @Value("${greeting.message}")
    private String message;



}
