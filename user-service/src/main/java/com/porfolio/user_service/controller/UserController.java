package com.porfolio.user_service.controller;

import com.porfolio.user_service.entity.User;
import com.porfolio.user_service.exception.UserAlreadyExistsException;
import com.porfolio.user_service.exception.UserNotFoundException;
import com.porfolio.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUser(@RequestHeader("X-User-Id") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateLoggedInUser(
            @RequestHeader("X-User-ID") Long userId,
            @RequestBody User updatedUserData) {

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        User existingUser = userOptional.get();

        // update only permitted fields
        if (updatedUserData.getName() != null && !updatedUserData.getName().isBlank()) {
            existingUser.setName(updatedUserData.getName());
        }
        if (updatedUserData.getEmail() != null && !updatedUserData.getEmail().isBlank()) {

            if (!existingUser.getEmail().equals(updatedUserData.getEmail()) &&
                    userRepository.findByEmail(updatedUserData.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("Email already in use: " + updatedUserData.getEmail());
            }
            existingUser.setEmail(updatedUserData.getEmail());
        }

        User savedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }

 
}