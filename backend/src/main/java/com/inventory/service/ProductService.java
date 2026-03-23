package com.inventory.service;
import com.inventory.dto.*;
import com.inventory.entity.*;
import com.inventory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional
public class ProductService {
    @Autowired private ProductRepository productRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private NotificationService notifService;

    public ApiResponse add(ProductRequest r, String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        if (r.getSku()!=null&&!r.getSku().isBlank()&&productRepo.existsBySkuAndAdminId(r.getSku(),admin.getId()))
            return new ApiResponse(false,"SKU already exists in your inventory",null);
        Product p = new Product(); map(r,p); p.setAdmin(admin);
        productRepo.save(p);
        if (p.isBelowThreshold()) notifService.create(admin,"Low Stock Alert","Product '"+p.getName()+"' is below threshold. Qty: "+p.getQuantity(),Notification.NotificationType.LOW_STOCK,p.getId(),"PRODUCT");
        return new ApiResponse(true,"Product added successfully",toDTO(p));
    }

    public ApiResponse update(Long id, ProductRequest r, String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        Product p = productRepo.findById(id).orElse(null);
        if (p==null||!p.getAdmin().getId().equals(admin.getId())) return new ApiResponse(false,"Product not found",null);
        map(r,p); p.setUpdatedAt(LocalDateTime.now()); productRepo.save(p);
        if (p.isBelowThreshold()) notifService.create(admin,"Low Stock Alert","Product '"+p.getName()+"' is below threshold. Qty: "+p.getQuantity(),Notification.NotificationType.LOW_STOCK,p.getId(),"PRODUCT");
        return new ApiResponse(true,"Product updated successfully",toDTO(p));
    }

    public ApiResponse delete(Long id, String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        Product p = productRepo.findById(id).orElse(null);
        if (p==null||!p.getAdmin().getId().equals(admin.getId())) return new ApiResponse(false,"Product not found",null);
        p.setActive(false); productRepo.save(p);
        return new ApiResponse(true,"Product deleted",null);
    }

    @Transactional(readOnly=true)
    public ApiResponse getAll(String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        return new ApiResponse(true,"OK",productRepo.findByAdminIdAndActiveTrue(admin.getId()).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly=true)
    public ApiResponse getById(Long id, String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        Product p = productRepo.findById(id).orElse(null);
        if (p==null||!p.getAdmin().getId().equals(admin.getId())||!p.isActive()) return new ApiResponse(false,"Product not found",null);
        return new ApiResponse(true,"OK",toDTO(p));
    }

    @Transactional(readOnly=true)
    public ApiResponse search(String adminUsername, String kw) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        return new ApiResponse(true,"OK",productRepo.search(admin.getId(),kw).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly=true)
    public ApiResponse lowStock(String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        return new ApiResponse(true,"OK",productRepo.findLowStock(admin.getId()).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly=true)
    public ApiResponse getByAdminUsername(String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null||admin.getRole()!=User.Role.ADMIN) return new ApiResponse(false,"Admin not found",null);
        return new ApiResponse(true,"OK",productRepo.findByAdminIdAndActiveTrue(admin.getId()).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    private void map(ProductRequest r, Product p) {
        p.setName(r.getName()); p.setDescription(r.getDescription()); p.setCategory(r.getCategory());
        p.setSku(r.getSku()); p.setQuantity(r.getQuantity()); p.setMinThreshold(r.getMinThreshold());
        p.setPrice(r.getPrice()); p.setUnit(r.getUnit());
    }

    public ProductResponse toDTO(Product p) {
        ProductResponse r = new ProductResponse();
        r.setId(p.getId()); r.setName(p.getName()); r.setDescription(p.getDescription());
        r.setCategory(p.getCategory()); r.setSku(p.getSku()); r.setQuantity(p.getQuantity());
        r.setMinThreshold(p.getMinThreshold()); r.setPrice(p.getPrice()); r.setUnit(p.getUnit());
        r.setLowStock(p.isBelowThreshold()); r.setCreatedAt(p.getCreatedAt()); r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}
