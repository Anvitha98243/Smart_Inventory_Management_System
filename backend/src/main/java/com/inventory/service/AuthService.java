package com.inventory.service;
import com.inventory.dto.*;
import com.inventory.entity.User;
import com.inventory.repository.UserRepository;
import com.inventory.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service @Transactional
public class AuthService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired private UserDetailsService uds;

    public ApiResponse register(RegisterRequest r) {
        if (userRepo.existsByUsername(r.getUsername())) return new ApiResponse(false,"Username already taken",null);
        if (userRepo.existsByEmail(r.getEmail())) return new ApiResponse(false,"Email already registered",null);
        User u = new User();
        u.setUsername(r.getUsername()); u.setPassword(encoder.encode(r.getPassword()));
        u.setEmail(r.getEmail()); u.setFullName(r.getFullName());
        try { u.setRole(User.Role.valueOf(r.getRole().toUpperCase())); }
        catch (Exception e) { return new ApiResponse(false,"Role must be ADMIN or STAFF",null); }
        if (u.getRole()==User.Role.STAFF) {
            if (r.getAdminUsername()==null||r.getAdminUsername().isBlank())
                return new ApiResponse(false,"Admin username required for staff",null);
            User admin = userRepo.findByUsername(r.getAdminUsername()).orElse(null);
            if (admin==null||admin.getRole()!=User.Role.ADMIN)
                return new ApiResponse(false,"Admin '"+r.getAdminUsername()+"' not found",null);
            u.setAdmin(admin);
        }
        userRepo.save(u);
        return new ApiResponse(true,"Registration successful",null);
    }

    public ApiResponse login(LoginRequest r) {
        try { authManager.authenticate(new UsernamePasswordAuthenticationToken(r.getUsername(),r.getPassword())); }
        catch (Exception e) { return new ApiResponse(false,"Invalid username or password",null); }
        User u = userRepo.findByUsername(r.getUsername()).orElse(null);
        if (u==null||!u.isActive()) return new ApiResponse(false,"Account inactive",null);
        String token = jwtUtil.generateToken(uds.loadUserByUsername(r.getUsername()));
        return new ApiResponse(true,"Login successful",new AuthResponse(token,u.getUsername(),u.getRole().name(),u.getFullName(),u.getId()));
    }

    public ApiResponse forgotPassword(ForgotPasswordRequest r) {
        User u = userRepo.findByEmail(r.getEmail()).orElse(null);
        if (u==null) return new ApiResponse(false,"No account found with this email",null);
        String token = UUID.randomUUID().toString();
        u.setResetToken(token); u.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepo.save(u);
        return new ApiResponse(true,"Reset token: "+token,null);
    }

    public ApiResponse resetPassword(ResetPasswordRequest r) {
        User u = userRepo.findByResetToken(r.getToken()).orElse(null);
        if (u==null) return new ApiResponse(false,"Invalid or expired token",null);
        if (u.getResetTokenExpiry().isBefore(LocalDateTime.now())) return new ApiResponse(false,"Token has expired",null);
        u.setPassword(encoder.encode(r.getNewPassword()));
        u.setResetToken(null); u.setResetTokenExpiry(null);
        userRepo.save(u);
        return new ApiResponse(true,"Password reset successful",null);
    }
}
