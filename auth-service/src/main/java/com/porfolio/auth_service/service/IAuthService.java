package com.porfolio.auth_service.service;

import com.porfolio.auth_service.dto.UserRegisterDTO;
import com.porfolio.auth_service.entity.User;

public interface IAuthService {
    User registerUser(String email, String name, String lastName, String password);

    User registerUser(UserRegisterDTO userRegisterDTO);

    String loginUser(String email, String password);
}
