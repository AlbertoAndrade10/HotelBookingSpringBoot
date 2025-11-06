package com.porfolio.auth_service.service;

import com.porfolio.auth_service.dto.UserRegisterDTO;
import com.porfolio.auth_service.entity.User;

public interface IAuthService {
  

    User registerUser(UserRegisterDTO userRegisterDTO);

    String loginUser(String email, String password);
}
