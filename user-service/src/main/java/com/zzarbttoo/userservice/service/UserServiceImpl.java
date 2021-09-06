package com.zzarbttoo.userservice.service;

import com.netflix.discovery.converters.Auto;
import com.zzarbttoo.userservice.dto.UserDto;
import com.zzarbttoo.userservice.jpa.UserEntity;
import com.zzarbttoo.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        // 설정 정보가 딱 맞아떨어져야지 변환 가능
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd("encrypted_password");

        userRepository.save(userEntity);

        //DB에 저장하기 위해서는 UserEntity 가 필요
        //Mapper : DTO Class -> Entity Class

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }
}
