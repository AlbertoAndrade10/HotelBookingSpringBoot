package com.porfolio.auth_service.mapper;

import com.porfolio.auth_service.dto.UserLoginDTO;
import com.porfolio.auth_service.dto.UserRegisterDTO;
import com.porfolio.auth_service.entity.Role;
import com.porfolio.auth_service.entity.User;

public class UserMapper {

    public static User toUserFromRegisterDto(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setEmail(userRegisterDTO.getEmail());
        user.setName(userRegisterDTO.getName());
        user.setLastName(userRegisterDTO.getLastName());
        user.setPassword(userRegisterDTO.getPassword());
        user.setRole(Role.USER.name());

        return user;
    }

    public static User toUserFromLoginDto(UserLoginDTO userLoginDTO) {
        User user = new User();
        user.setEmail(userLoginDTO.getEmail());
        user.setPassword(userLoginDTO.getPassword());

        return user;
    }

}
