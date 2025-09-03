package com.example.inventorysystem.user.service;

import com.example.inventorysystem.user.dto.LoginRequest;
import com.example.inventorysystem.user.dto.RegisterRequest;
import com.example.inventorysystem.user.model.User;
import com.example.inventorysystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequest request) {
        try {
            // Check if username already exists
            if (userRepository.existsByUsername(request.getUsername())) {
                return "Username already exists";
            }

            // Check if email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                return "Email already exists";
            }

            // Create new user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCreatedDate(LocalDateTime.now());
            user.setIsActive(true);

            userRepository.save(user);
            return "User registered successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Registration failed: " + e.getMessage();
        }
    }

    public String login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getIsActive() && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return "Login successful";
            }
        }
        return "Invalid credentials";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.searchByKeyword(keyword);
    }

    public String updateUser(Integer userId, User updatedUser) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                if (updatedUser.getUsername() != null) {
                    user.setUsername(updatedUser.getUsername());
                }
                if (updatedUser.getEmail() != null) {
                    user.setEmail(updatedUser.getEmail());
                }
                if (updatedUser.getIsActive() != null) {
                    user.setIsActive(updatedUser.getIsActive());
                }
                
                userRepository.save(user);
                return "User updated successfully";
            } else {
                return "User not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Update failed: " + e.getMessage();
        }
    }

    public String deleteUser(Integer userId) {
        try {
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
                return "User deleted successfully";
            } else {
                return "User not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Delete failed: " + e.getMessage();
        }
    }
}
