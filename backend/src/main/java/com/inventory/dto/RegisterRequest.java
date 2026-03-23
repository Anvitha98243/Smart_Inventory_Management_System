package com.inventory.dto;
import jakarta.validation.constraints.*;
public class RegisterRequest {
    @NotBlank public String username;
    @NotBlank @Size(min=6) public String password;
    @NotBlank @Email public String email;
    @NotBlank public String fullName;
    @NotBlank public String role;
    public String adminUsername;
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public String getEmail(){ return email; }
    public String getFullName(){ return fullName; }
    public String getRole(){ return role; }
    public String getAdminUsername(){ return adminUsername; }
}
