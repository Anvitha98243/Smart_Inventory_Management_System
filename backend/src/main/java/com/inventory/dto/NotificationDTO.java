package com.inventory.dto;
import java.time.LocalDateTime;
public class NotificationDTO {
    public Long id; public String title, message, type; public boolean read; public LocalDateTime createdAt;
    public NotificationDTO(){}
    public NotificationDTO(Long id, String title, String message, String type, boolean read, LocalDateTime createdAt){
        this.id=id; this.title=title; this.message=message; this.type=type; this.read=read; this.createdAt=createdAt;
    }
    public Long getId(){ return id; } public String getTitle(){ return title; }
    public String getMessage(){ return message; } public String getType(){ return type; }
    public boolean isRead(){ return read; } public LocalDateTime getCreatedAt(){ return createdAt; }
}
