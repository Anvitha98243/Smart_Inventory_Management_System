package com.inventory.dto;
import jakarta.validation.constraints.*;
public class LoginRequest {
    @NotBlank public String username;
    @NotBlank public String password;
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
}
