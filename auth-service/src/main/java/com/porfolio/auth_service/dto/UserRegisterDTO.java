package com.porfolio.auth_service.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String email;
    private String name;
    private String lastName;
    private String password;

}
