package com.inventory.dto;
public class AuthResponse {
    public String token, username, role, fullName;
    public Long userId;
    public AuthResponse(){}
    public AuthResponse(String token, String username, String role, String fullName, Long userId){
        this.token=token; this.username=username; this.role=role; this.fullName=fullName; this.userId=userId;
    }
    public String getToken(){ return token; }
    public String getUsername(){ return username; }
    public String getRole(){ return role; }
    public String getFullName(){ return fullName; }
    public Long getUserId(){ return userId; }
}
