package com.inventory.service;
import com.inventory.dto.*;
import com.inventory.entity.Notification;
import com.inventory.entity.User;
import com.inventory.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional
public class NotificationService {
    @Autowired private NotificationRepository repo;

    public void create(User user, String title, String message, Notification.NotificationType type, Long relatedId, String relatedType) {
        Notification n = new Notification();
        n.setUser(user); n.setTitle(title); n.setMessage(message);
        n.setType(type); n.setRelatedEntityId(relatedId); n.setRelatedEntityType(relatedType);
        repo.save(n);
    }

    @Transactional(readOnly=true)
    public List<NotificationDTO> getAll(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly=true)
    public long unreadCount(Long userId) { return repo.countByUserIdAndReadFalse(userId); }

    public ApiResponse markRead(Long id, Long userId) {
        Notification n = repo.findById(id).orElse(null);
        if (n==null || !n.getUser().getId().equals(userId)) return new ApiResponse(false,"Not found",null);
        n.setRead(true); repo.save(n);
        return new ApiResponse(true,"Marked read",null);
    }

    public ApiResponse markAllRead(Long userId) {
        List<Notification> list = repo.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        list.forEach(n->n.setRead(true)); repo.saveAll(list);
        return new ApiResponse(true,"All read",null);
    }

    private NotificationDTO toDTO(Notification n) {
        return new NotificationDTO(n.getId(),n.getTitle(),n.getMessage(),n.getType().name(),n.isRead(),n.getCreatedAt());
    }
}
