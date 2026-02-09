package com.inventory.service;

import com.inventory.model.*;
import com.inventory.repository.UserRepository;
import com.inventory.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EmailService emailService;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // User Registration
    @Transactional
    public AuthResponse registerUser(SignupRequest signupRequest) {
        try {
            // Check if username exists
            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                return new AuthResponse(false, "Username already exists!");
            }
            
            // Check if email exists
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return new AuthResponse(false, "Email already exists!");
            }
            
            // Create new user
            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setFullName(signupRequest.getFullName());
            user.setRole(signupRequest.getRole() != null ? signupRequest.getRole() : "EMPLOYEE");
            user.setIsActive(true);
            
            User savedUser = userRepository.save(user);
            
            // Send welcome email (non-blocking)
            try {
                emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFullName(), savedUser.getUsername());
            } catch (Exception e) {
                System.err.println("⚠️ Failed to send welcome email, but registration succeeded: " + e.getMessage());
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole());
            
            return new AuthResponse(true, "User registered successfully!", token, new UserDTO(savedUser));
            
        } catch (Exception e) {
            return new AuthResponse(false, "Registration failed: " + e.getMessage());
        }
    }
    
    // User Login
    @Transactional
    public AuthResponse loginUser(LoginRequest loginRequest) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
            
            if (!userOptional.isPresent()) {
                return new AuthResponse(false, "Invalid username or password!");
            }
            
            User user = userOptional.get();
            
            // Check if user is active
            if (!user.getIsActive()) {
                return new AuthResponse(false, "Account is deactivated. Contact administrator.");
            }
            
            // Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return new AuthResponse(false, "Invalid username or password!");
            }
            
            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
            
            return new AuthResponse(true, "Login successful!", token, new UserDTO(user));
            
        } catch (Exception e) {
            return new AuthResponse(false, "Login failed: " + e.getMessage());
        }
    }
    
    // Forgot Password - Generate Reset Token and Send Email
    @Transactional
    public AuthResponse forgotPassword(String email) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            
            if (!userOptional.isPresent()) {
                return new AuthResponse(false, "Email not found!");
            }
            
            User user = userOptional.get();
            
            // Generate reset token
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
            
            userRepository.save(user);
            
            // Send password reset email
            try {
                emailService.sendPasswordResetEmail(user.getEmail(), resetToken, user.getFullName());
                return new AuthResponse(true, "Password reset email sent! Please check your inbox.");
            } catch (Exception e) {
                System.err.println("❌ Failed to send email: " + e.getMessage());
                // Return token in response as fallback (for testing)
                return new AuthResponse(true, "Email sending failed. Reset token: " + resetToken + " (Valid for 1 hour)");
            }
            
        } catch (Exception e) {
            return new AuthResponse(false, "Failed to generate reset token: " + e.getMessage());
        }
    }
    
    // Reset Password
    @Transactional
    public AuthResponse resetPassword(ResetPasswordRequest resetRequest) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(resetRequest.getEmail());
            
            if (!userOptional.isPresent()) {
                return new AuthResponse(false, "Email not found!");
            }
            
            User user = userOptional.get();
            
            // Verify reset token
            if (user.getResetToken() == null || !user.getResetToken().equals(resetRequest.getResetToken())) {
                return new AuthResponse(false, "Invalid reset token!");
            }
            
            // Check if token expired
            if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                return new AuthResponse(false, "Reset token has expired!");
            }
            
            // Update password
            user.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            
            userRepository.save(user);
            
            return new AuthResponse(true, "Password reset successful!");
            
        } catch (Exception e) {
            return new AuthResponse(false, "Password reset failed: " + e.getMessage());
        }
    }
    
    // Get all users (Admin only)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }
    
    // Get user by ID
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::new);
    }
    
    // Delete user (Admin only)
    @Transactional
    public AuthResponse deleteUser(Long id) {
        try {
            if (!userRepository.existsById(id)) {
                return new AuthResponse(false, "User not found!");
            }
            
            userRepository.deleteById(id);
            return new AuthResponse(true, "User deleted successfully!");
            
        } catch (Exception e) {
            return new AuthResponse(false, "Failed to delete user: " + e.getMessage());
        }
    }
    
    // Deactivate user (Admin only)
    @Transactional
    public AuthResponse deactivateUser(Long id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            
            if (!userOptional.isPresent()) {
                return new AuthResponse(false, "User not found!");
            }
            
            User user = userOptional.get();
            user.setIsActive(false);
            userRepository.save(user);
            
            return new AuthResponse(true, "User deactivated successfully!");
            
        } catch (Exception e) {
            return new AuthResponse(false, "Failed to deactivate user: " + e.getMessage());
        }
    }
}
