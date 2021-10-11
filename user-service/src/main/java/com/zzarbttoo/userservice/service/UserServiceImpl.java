package com.zzarbttoo.userservice.service;

import com.zzarbttoo.userservice.dto.UserDto;
import com.zzarbttoo.userservice.jpa.UserEntity;
import com.zzarbttoo.userservice.jpa.UserRepository;
import com.zzarbttoo.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        //email로 찾는다
        UserEntity userEntity = userRepository.findByEmail(userName);

        //해당하는 사용자가 없다
        if (userEntity == null){
            throw new UsernameNotFoundException(userName); //spirng security에서 제공
        }

        //맨 마지막에는 권한을 넣어주면 된다
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>()); //검색-> password -> 반환
    }

    //인자들도 Bean 등록이 되어야 메모리에 적재를 할 수 있음
    //userRepository는 Bean 등록이 되어있는데, BCryptPasswordEncoder은 등록이 되어있지 않음
    //가장 먼저 등록이 진행되는 곳에 BCryptPasswordEncoder Bean 등록
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        // 설정 정보가 딱 맞아떨어져야지 변환 가능
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        //userEntity.setEncryptedPwd("encrypted_password");

        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        //DB에 저장하기 위해서는 UserEntity 가 필요
        //Mapper : DTO Class -> Entity Class

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null){
            //spring security
            throw new UsernameNotFoundException("User not found");
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);


        return userDto;
    }
}
