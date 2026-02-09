package com.inventory.controller;

import com.inventory.model.*;
import com.inventory.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Authentication API is working!");
    }
    
    // User Registration
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody SignupRequest signupRequest) {
        AuthResponse response = authService.registerUser(signupRequest);
        return ResponseEntity.ok(response);
    }
    
    // User Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    // Forgot Password - Request Reset Token
    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        AuthResponse response = authService.forgotPassword(email);
        return ResponseEntity.ok(response);
    }
    
    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordRequest resetRequest) {
        AuthResponse response = authService.resetPassword(resetRequest);
        return ResponseEntity.ok(response);
    }
    
    // Get all users (Admin only)
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return authService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Delete user (Admin only)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<AuthResponse> deleteUser(@PathVariable Long id) {
        AuthResponse response = authService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
    
    // Deactivate user (Admin only)
    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<AuthResponse> deactivateUser(@PathVariable Long id) {
        AuthResponse response = authService.deactivateUser(id);
        return ResponseEntity.ok(response);
    }
}
