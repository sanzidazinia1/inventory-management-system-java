package com.example.inventorysystem.user.controller;

import com.example.inventorysystem.user.dto.LoginRequest;
import com.example.inventorysystem.user.dto.RegisterRequest;
import com.example.inventorysystem.user.model.User;
import com.example.inventorysystem.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = userService.register(request);
            if (result.equals("User registered successfully")) {
                response.put("success", true);
                response.put("message", result);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", result);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = userService.login(request);
            if (result.equals("Login successful")) {
                // Get user details for session
                Optional<User> userOpt = userService.findByUsername(request.getUsername());
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    // Store user info in session
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("username", user.getUsername());
                    
                    response.put("success", true);
                    response.put("message", result);
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("userId", user.getUserId());
                    userData.put("username", user.getUsername());
                    userData.put("email", user.getEmail());
                    response.put("user", userData);
                    return ResponseEntity.ok(response);
                }
            }
            response.put("success", false);
            response.put("message", result);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            session.invalidate();
            response.put("success", true);
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Logout failed");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "Not logged in");
                return ResponseEntity.badRequest().body(response);
            }
            
            Optional<User> userOpt = userService.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> userData = new HashMap<>();
                userData.put("userId", user.getUserId());
                userData.put("username", user.getUsername());
                userData.put("email", user.getEmail());
                userData.put("isActive", user.getIsActive());
                userData.put("createdDate", user.getCreatedDate());
                
                response.put("success", true);
                response.put("data", userData);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get profile: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        
        if (userId != null && username != null) {
            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("userId", userId);
            sessionData.put("username", username);
            
            response.put("success", true);
            response.put("loggedIn", true);
            response.put("user", sessionData);
        } else {
            response.put("success", true);
            response.put("loggedIn", false);
        }
        return ResponseEntity.ok(response);
    }
}
