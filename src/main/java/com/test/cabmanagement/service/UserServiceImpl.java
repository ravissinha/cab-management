package com.test.cabmanagement.service;

import com.test.cabmanagement.dto.UserDTO;
import com.test.cabmanagement.entity.UserEntity;
import com.test.cabmanagement.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        UserEntity userEntity = UserEntity.builder().email(userDTO.getEmail()).firstName(userDTO.getFirstName()).
                lastName(userDTO.getLastName()).phoneNumber(userDTO.getPhoneNumber()).gender(userDTO.getGender()).
                userName(userDTO.getUserName()).createdDate(new Timestamp(System.currentTimeMillis())).
                password(userDTO.getPassword()).build();
        userEntity =userJpaRepository.save(userEntity);
        userDTO.setId(userEntity.getId());
        return userDTO;
    }

}
