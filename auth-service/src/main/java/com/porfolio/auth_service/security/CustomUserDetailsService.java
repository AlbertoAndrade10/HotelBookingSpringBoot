package com.porfolio.auth_service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import com.porfolio.auth_service.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("CustomUserDetailsService: Cargando usuario por email: {}", email);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("CustomUserDetailsService: Usuario no encontrado con email: {}", email);
                    return new UsernameNotFoundException("User not found: " + email);
                });
        logger.info("CustomUserDetailsService: Usuario encontrado con email: {}", user.getEmail());
        return User.builder()
       .username(user.getEmail())
       .password(user.getPassword())
       .authorities(user.getRole())
       .accountExpired(false)
       .accountLocked(false)
       .credentialsExpired(false)
       .disabled(false)
       .build();
    }
}