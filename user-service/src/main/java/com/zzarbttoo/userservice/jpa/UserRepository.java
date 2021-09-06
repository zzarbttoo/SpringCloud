package com.zzarbttoo.userservice.jpa;

import org.springframework.data.repository.CrudRepository;

//CRUD 작업을 하기 때문에 CrudRepository 상속
//Entity 정보 입력, 기본키 classType 입력
public interface UserRepository extends CrudRepository<UserEntity, Long> {

}
