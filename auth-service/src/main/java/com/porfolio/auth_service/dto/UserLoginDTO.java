package com.porfolio.auth_service.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}
