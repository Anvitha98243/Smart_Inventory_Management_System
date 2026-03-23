package com.inventory.dto;
import jakarta.validation.constraints.*;
public class ResetPasswordRequest {
    @NotBlank public String token;
    @NotBlank @Size(min=6) public String newPassword;
    public String getToken(){ return token; }
    public String getNewPassword(){ return newPassword; }
}
