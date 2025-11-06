package com.porfolio.auth_service.service;

import com.porfolio.auth_service.dto.UserRegisterDTO;
import com.porfolio.auth_service.entity.User;
import com.porfolio.auth_service.exception.InvalidCredentialsException;
import com.porfolio.auth_service.exception.UserAlreadyExistsException;
import com.porfolio.auth_service.mapper.UserMapper;
import com.porfolio.auth_service.repository.UserRepository;
import com.porfolio.auth_service.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String loginUser(String email, String password) {
        logger.info("AuthServiceImpl: Iniciando login para email: {}", email);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            String token = jwtUtil
                    .generateToken((org.springframework.security.core.userdetails.User) authentication.getPrincipal());
            logger.info("AuthServiceImpl: Login exitoso para email: {}", email);
            return token;
        } catch (Exception e) {
            logger.error("AuthServiceImpl: Error de autenticación para email: {}", email, e);

            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @Override
    public User registerUser(UserRegisterDTO registerDTO) {
        logger.info("AuthServiceImpl: Registrando usuario con DTO para email: {}", registerDTO.getEmail());

        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            logger.error("AuthServiceImpl: Usuario con email {} ya existe.", registerDTO.getEmail());

            throw new UserAlreadyExistsException("User already exists with email: " + registerDTO.getEmail());
        }

        User userToSave = UserMapper.toUserFromRegisterDto(registerDTO);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        User savedUser = userRepository.save(userToSave);
        logger.info("AuthServiceImpl: Usuario guardado con ID: {}", savedUser.getId());
        return savedUser;
    }
}
