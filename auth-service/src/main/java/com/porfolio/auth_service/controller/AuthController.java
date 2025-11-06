// src/main/java/com/porfolio/auth_service/controller/AuthController.java
package com.porfolio.auth_service.controller;

import com.porfolio.auth_service.dto.UserLoginDTO;
import com.porfolio.auth_service.dto.UserRegisterDTO;
import com.porfolio.auth_service.service.IAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO request) {
        logger.info("AuthController: Entrando en /auth/register con email: {}", request.getEmail());

        var user = authService.registerUser(request);

        logger.info("AuthController: Usuario registrado exitosamente con ID: {}", user.getId());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO request) {
        logger.info("AuthController: Entrando en /auth/login con email: {}", request.getEmail());
    
        String token = authService.loginUser(request.getEmail(), request.getPassword());
        
        logger.info("AuthController: Login exitoso para email: {}", request.getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}