package com.inventory.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(unique=true, nullable=false, length=50) private String username;
    @Column(nullable=false) private String password;
    @Column(unique=true, nullable=false, length=100) private String email;
    @Column(nullable=false, length=100) private String fullName;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=10) private Role role;
    @Column(length=500) private String resetToken;
    private LocalDateTime resetTokenExpiry;
    @Column(nullable=false) private boolean active = true;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="admin_id") private User admin;

    public enum Role { ADMIN, STAFF }

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public Role getRole(){return role;} public void setRole(Role v){role=v;}
    public String getResetToken(){return resetToken;} public void setResetToken(String v){resetToken=v;}
    public LocalDateTime getResetTokenExpiry(){return resetTokenExpiry;} public void setResetTokenExpiry(LocalDateTime v){resetTokenExpiry=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    public User getAdmin(){return admin;} public void setAdmin(User v){admin=v;}
}
