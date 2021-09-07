package com.zzarbttoo.userservice.service;

import com.zzarbttoo.userservice.dto.UserDto;
import com.zzarbttoo.userservice.jpa.UserEntity;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

}
