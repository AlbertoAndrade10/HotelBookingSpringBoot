package com.porfolio.auth_service.service;

import com.porfolio.auth_service.dto.UserRegisterDTO;
import com.porfolio.auth_service.entity.User;
import com.porfolio.auth_service.mapper.UserMapper;
import com.porfolio.auth_service.repository.UserRepository;
import com.porfolio.auth_service.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    public User registerUser(String email, String name, String lastName, String password) {
        logger.info("AuthServiceImpl: Intentando registrar usuario con email: {}", email);

        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("AuthServiceImpl: Usuario ya existe con email: {}", email);
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");

        logger.info("AuthServiceImpl: Contraseña encriptada. Guardando usuario en base de datos...");
        User savedUser = userRepository.save(user);
        logger.info("AuthServiceImpl: Usuario guardado exitosamente con ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public String loginUser(String email, String password) {
        logger.info("AuthServiceImpl: Intentando login para email: {}", email);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManager.authenticate(authToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        logger.info("AuthServiceImpl: Autenticación exitosa para email: {}", userDetails.getUsername());

        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public User registerUser(UserRegisterDTO registerDTO) {
        logger.info("AuthServiceImpl: Registrando usuario con DTO para email: {}", registerDTO.getEmail());
        
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            logger.error("AuthServiceImpl: Usuario con email {} ya existe.", registerDTO.getEmail());
            throw new RuntimeException("User already exists with email: " + registerDTO.getEmail());
        }
        
        User userToSave = UserMapper.toUserFromRegisterDto(registerDTO);
       
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        User savedUser = userRepository.save(userToSave);
        logger.info("AuthServiceImpl: Usuario guardado con ID: {}", savedUser.getId());
        return savedUser;
    }
}
