package com.dhillon.twitterclone.controller;

import com.dhillon.twitterclone.entity.User;
import com.dhillon.twitterclone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller that handles authentication-related endpoints like login, logout, token refresh, etc.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Login endpoint that authenticates users and returns a token.
     *
     * @param loginRequest the login request with username and password
     * @return the authentication response with token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());
        
        // Find the user by username
        Optional<User> userOpt = userService.findByUsername(loginRequest.getUsername());
        
        // If user not found or password doesn't match, return unauthorized
        if (userOpt.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPasswordHash())) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 401);
            errorResponse.put("error", "Unauthorized");
            errorResponse.put("message", "Invalid username or password");
            errorResponse.put("path", request.getRequestURI());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        
        User user = userOpt.get();
        
        // In a full implementation, we would generate and return a JWT token here
        // For now, just return a simple success response with user info
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "email", user.getEmail(),
            "displayName", user.getDisplayName(),
            // Placeholder for JWT token
            "token", "dummy-token-" + System.currentTimeMillis()
        ));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Request body for login endpoint.
     */
    public static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
} 