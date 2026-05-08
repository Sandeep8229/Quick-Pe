package com.quickpe.controller;

import com.quickpe.dto.AuthResponse;
import com.quickpe.dto.LoginRequest;
import com.quickpe.dto.SignUpRequest;
import com.quickpe.entity.User;
import com.quickpe.security.JwtTokenProvider;
import com.quickpe.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * AuthenticationController - REST API for authentication
 * Handles user registration and login
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Register new user
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            User user = authenticationService.registerUser(signUpRequest);
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Login user
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = authenticationService.loginUser(loginRequest);
            User user = authenticationService.getUserById(
                    jwtTokenProvider.getUserIdFromToken(token)
            );
            AuthResponse authResponse = new AuthResponse(token, user.getId(), user.getUsername());
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Login failed", null));
        }
    }

    /**
     * API Response wrapper class
     */
    public static class ApiResponse {
        private Boolean success;
        private String message;
        private Object data;

        public ApiResponse(Boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public Boolean getSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}
