package com.porfolio.auth_service.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import com.porfolio.auth_service.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

       return User.builder()
       .username(user.getEmail())
       .password(user.getPassword())
       .authorities("USER")
       .accountExpired(false)
       .accountLocked(false)
       .credentialsExpired(false)
       .disabled(false)
       .build();
    }
    
}
