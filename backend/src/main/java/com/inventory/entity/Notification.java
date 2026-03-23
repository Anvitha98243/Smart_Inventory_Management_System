package com.inventory.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="user_id", nullable=false) private User user;
    @Column(nullable=false, length=200) private String title;
    @Column(nullable=false, length=1000) private String message;
    @Enumerated(EnumType.STRING) @Column(length=30) private NotificationType type = NotificationType.INFO;
    @Column(name="is_read", nullable=false) private boolean read = false;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    private Long relatedEntityId;
    @Column(length=50) private String relatedEntityType;

    public enum NotificationType { LOW_STOCK, STOCK_REQUEST, REQUEST_UPDATE, INFO, WARNING }

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public User getUser(){return user;} public void setUser(User v){user=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
    public NotificationType getType(){return type;} public void setType(NotificationType v){type=v;}
    public boolean isRead(){return read;} public void setRead(boolean v){read=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    public Long getRelatedEntityId(){return relatedEntityId;} public void setRelatedEntityId(Long v){relatedEntityId=v;}
    public String getRelatedEntityType(){return relatedEntityType;} public void setRelatedEntityType(String v){relatedEntityType=v;}
}
