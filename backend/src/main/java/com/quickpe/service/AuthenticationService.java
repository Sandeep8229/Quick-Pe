package com.quickpe.service;

import com.quickpe.dto.LoginRequest;
import com.quickpe.dto.SignUpRequest;
import com.quickpe.entity.User;
import com.quickpe.entity.Wallet;
import com.quickpe.repository.UserRepository;
import com.quickpe.security.JwtTokenProvider;
import com.quickpe.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * AuthenticationService - Handles user registration and login
 */
@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Register new user
     */
    public User registerUser(SignUpRequest signUpRequest) {
        // Validate email doesn't exist
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Validate username doesn't exist
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        // Validate mobile number doesn't exist
        if (userRepository.existsByMobileNumber(signUpRequest.getMobileNumber())) {
            throw new RuntimeException("Mobile number already registered");
        }

        // Create new user
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .mobileNumber(signUpRequest.getMobileNumber())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .isActive(true)
                .isVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        // Create wallet for new user
        walletService.createWallet(savedUser);

        return savedUser;
    }

    /**
     * Login user and generate JWT token
     */
    public String loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        return jwtTokenProvider.generateToken(authentication);
    }

    /**
     * Get user by ID
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Update user profile
     */
    public User updateUserProfile(Long userId, User updatedUser) {
        User user = getUserById(userId);

        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            user.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(updatedUser.getProfilePictureUrl());
        }

        return userRepository.save(user);
    }
}
