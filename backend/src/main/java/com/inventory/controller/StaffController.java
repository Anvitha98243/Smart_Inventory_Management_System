package com.inventory.controller;
import com.inventory.dto.*;
import com.inventory.entity.User;
import com.inventory.repository.UserRepository;
import com.inventory.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    @Autowired private StockRequestService reqService;
    @Autowired private ProductService productService;
    @Autowired private NotificationService notifService;
    @Autowired private UserRepository userRepo;

    @PostMapping("/requests")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody StockRequestCreate r, Authentication a) { return ResponseEntity.ok(reqService.create(r,a.getName())); }

    @GetMapping("/requests")
    public ResponseEntity<ApiResponse> mine(Authentication a) { return ResponseEntity.ok(reqService.getForStaff(a.getName())); }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse> products(@RequestParam String adminUsername) { return ResponseEntity.ok(productService.getByAdminUsername(adminUsername)); }

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse> getNotifs(Authentication a) {
        User u = userRepo.findByUsername(a.getName()).orElse(null);
        if (u==null) return ResponseEntity.ok(new ApiResponse(false,"Not found",null));
        return ResponseEntity.ok(new ApiResponse(true,"OK",notifService.getAll(u.getId())));
    }

    @GetMapping("/notifications/unread-count")
    public ResponseEntity<ApiResponse> unreadCount(Authentication a) {
        User u = userRepo.findByUsername(a.getName()).orElse(null);
        if (u==null) return ResponseEntity.ok(new ApiResponse(false,"Not found",null));
        return ResponseEntity.ok(new ApiResponse(true,"OK",notifService.unreadCount(u.getId())));
    }

    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<ApiResponse> markRead(@PathVariable Long id, Authentication a) {
        User u = userRepo.findByUsername(a.getName()).orElse(null);
        if (u==null) return ResponseEntity.ok(new ApiResponse(false,"Not found",null));
        return ResponseEntity.ok(notifService.markRead(id,u.getId()));
    }

    @PutMapping("/notifications/read-all")
    public ResponseEntity<ApiResponse> markAllRead(Authentication a) {
        User u = userRepo.findByUsername(a.getName()).orElse(null);
        if (u==null) return ResponseEntity.ok(new ApiResponse(false,"Not found",null));
        return ResponseEntity.ok(notifService.markAllRead(u.getId()));
    }
}
