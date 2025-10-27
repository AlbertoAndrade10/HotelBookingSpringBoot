package com.porfolio.auth_service.controller;

import com.porfolio.auth_service.service.IAuthService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Importar SLF4J
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Logger

    @Autowired
    private IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("AuthController: Entrando en /auth/register con email: {}", request.getEmail());
        try {
            var user = authService.registerUser(request.getEmail(), request.getName(), request.getLastName(), request.getPassword());
            logger.info("AuthController: Usuario registrado exitosamente con ID: {}", user.getId());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            logger.error("AuthController: Error en /auth/register", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        logger.info("AuthController: Entrando en /auth/login con email: {}", request.getEmail());
        try {
            String token = authService.loginUser(request.getEmail(), request.getPassword());
            logger.info("AuthController: Login exitoso para email: {}", request.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException e) {
            logger.error("AuthController: Error en /auth/login", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // aux class

    @Data
    public static class RegisterRequest {
        private String email;
        private String name;
        private String lastName;
        private String password;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
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