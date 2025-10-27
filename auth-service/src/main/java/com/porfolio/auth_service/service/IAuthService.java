package com.porfolio.auth_service.service;

import com.porfolio.auth_service.entity.User;

public interface IAuthService {
    User registerUser(String email, String name, String password);
    String loginUser(String email, String password);
}
