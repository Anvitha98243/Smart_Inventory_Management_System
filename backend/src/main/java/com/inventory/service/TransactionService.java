package com.inventory.service;
import com.inventory.dto.*;
import com.inventory.entity.*;
import com.inventory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional
public class TransactionService {
    @Autowired private TransactionRepository txRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private NotificationService notifService;

    public ApiResponse create(TransactionRequest r, String username) {
        User performer = userRepo.findByUsername(username).orElse(null);
        if (performer==null) return new ApiResponse(false,"User not found",null);
        Product p = productRepo.findById(r.getProductId()).orElse(null);
        if (p==null||!p.isActive()) return new ApiResponse(false,"Product not found",null);
        User admin = performer.getRole()==User.Role.ADMIN ? performer : performer.getAdmin();
        if (admin==null||!p.getAdmin().getId().equals(admin.getId()))
            return new ApiResponse(false,"Product not in your inventory",null);
        Transaction.TransactionType type;
        try { type = Transaction.TransactionType.valueOf(r.getType().toUpperCase()); }
        catch (Exception e) { return new ApiResponse(false,"Invalid type. Use: PURCHASE, SALE, ADJUSTMENT, STOCK_IN, STOCK_OUT",null); }
        int before = p.getQuantity(), after = before;
        switch (type) {
            case PURCHASE: case STOCK_IN: after = before + r.getQuantity(); break;
            case SALE: case STOCK_OUT:
                if (before < r.getQuantity()) return new ApiResponse(false,"Insufficient stock. Available: "+before,null);
                after = before - r.getQuantity(); break;
            case ADJUSTMENT: after = r.getQuantity(); break;
        }
        p.setQuantity(after); p.setUpdatedAt(LocalDateTime.now()); productRepo.save(p);
        BigDecimal unitPrice = r.getUnitPrice()!=null ? r.getUnitPrice() : p.getPrice();
        Transaction tx = new Transaction();
        tx.setProduct(p); tx.setType(type); tx.setQuantity(r.getQuantity());
        tx.setUnitPrice(unitPrice); tx.setTotalAmount(unitPrice.multiply(BigDecimal.valueOf(r.getQuantity())));
        tx.setNotes(r.getNotes()); tx.setPerformedBy(performer); tx.setAdmin(admin);
        tx.setQuantityBefore(before); tx.setQuantityAfter(after);
        txRepo.save(tx);
        if (p.isBelowThreshold()) notifService.create(admin,"Low Stock Alert","Product '"+p.getName()+"' dropped below threshold. Qty: "+after,Notification.NotificationType.LOW_STOCK,p.getId(),"PRODUCT");
        return new ApiResponse(true,"Transaction recorded",toDTO(tx));
    }

    @Transactional(readOnly=true)
    public ApiResponse getAll(String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        return new ApiResponse(true,"OK",txRepo.findByAdminIdOrderByCreatedAtDesc(admin.getId()).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly=true)
    public ApiResponse getByProduct(Long productId, String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        return new ApiResponse(true,"OK",txRepo.findByProductIdOrderByCreatedAtDesc(productId).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly=true)
    public ApiResponse getByRange(String adminUsername, String start, String end) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        LocalDateTime s = LocalDate.parse(start).atStartOfDay();
        LocalDateTime e = LocalDate.parse(end).atTime(23,59,59);
        return new ApiResponse(true,"OK",txRepo.findByRange(admin.getId(),s,e).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly=true)
    public DashboardStats getDashboardStats(String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new DashboardStats();
        LocalDateTime start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        long products = productRepo.findByAdminIdAndActiveTrue(admin.getId()).size();
        long lowStock = productRepo.findLowStock(admin.getId()).size();
        BigDecimal sales = txRepo.sumSales(admin.getId(),start,now);
        BigDecimal purchases = txRepo.sumPurchases(admin.getId(),start,now);
        long txCount = txRepo.findByAdminIdOrderByCreatedAtDesc(admin.getId()).size();
        return new DashboardStats(products,lowStock,0,
            sales!=null?sales:BigDecimal.ZERO,
            purchases!=null?purchases:BigDecimal.ZERO,txCount);
    }

    private TransactionResponse toDTO(Transaction t) {
        TransactionResponse r = new TransactionResponse();
        r.setId(t.getId()); r.setProductName(t.getProduct().getName()); r.setProductId(t.getProduct().getId());
        r.setType(t.getType().name()); r.setQuantity(t.getQuantity()); r.setUnitPrice(t.getUnitPrice());
        r.setTotalAmount(t.getTotalAmount()); r.setNotes(t.getNotes());
        r.setPerformedBy(t.getPerformedBy().getFullName());
        r.setQuantityBefore(t.getQuantityBefore()); r.setQuantityAfter(t.getQuantityAfter());
        r.setCreatedAt(t.getCreatedAt());
        return r;
    }
}
