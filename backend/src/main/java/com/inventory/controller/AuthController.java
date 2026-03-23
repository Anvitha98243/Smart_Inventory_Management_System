package com.inventory.controller;
import com.inventory.dto.*;
import com.inventory.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest r) { return ResponseEntity.ok(authService.register(r)); }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest r) { return ResponseEntity.ok(authService.login(r)); }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgot(@Valid @RequestBody ForgotPasswordRequest r) { return ResponseEntity.ok(authService.forgotPassword(r)); }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> reset(@Valid @RequestBody ResetPasswordRequest r) { return ResponseEntity.ok(authService.resetPassword(r)); }
}
