package com.inventory.dto;
import jakarta.validation.constraints.*;
public class ForgotPasswordRequest {
    @NotBlank @Email public String email;
    public String getEmail(){ return email; }
}
