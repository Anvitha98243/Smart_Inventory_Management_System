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
public class StockRequestService {
    @Autowired private StockRequestRepository reqRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private NotificationService notifService;
    @Autowired private TransactionService txService;

    public ApiResponse create(StockRequestCreate r, String staffUsername) {
        User staff = userRepo.findByUsername(staffUsername).orElse(null);
        if (staff==null) return new ApiResponse(false,"Staff not found",null);
        User admin = userRepo.findByUsername(r.getAdminUsername()).orElse(null);
        if (admin==null||admin.getRole()!=User.Role.ADMIN)
            return new ApiResponse(false,"Admin '"+r.getAdminUsername()+"' not found",null);
        Product p = productRepo.findById(r.getProductId()).orElse(null);
        if (p==null||!p.isActive()||!p.getAdmin().getId().equals(admin.getId()))
            return new ApiResponse(false,"Product not found in admin's inventory",null);
        StockRequest.RequestType type;
        try { type = StockRequest.RequestType.valueOf(r.getRequestType().toUpperCase()); }
        catch (Exception e) { return new ApiResponse(false,"Type must be STOCK_IN or STOCK_OUT",null); }
        StockRequest sr = new StockRequest();
        sr.setProduct(p); sr.setStaff(staff); sr.setAdmin(admin);
        sr.setRequestType(type); sr.setQuantity(r.getQuantity()); sr.setReason(r.getReason());
        reqRepo.save(sr);
        notifService.create(admin,"New Stock Request",
            "Staff "+staff.getFullName()+" requested "+type.name().replace("_"," ")+" of "+r.getQuantity()+" units for '"+p.getName()+"'",
            Notification.NotificationType.STOCK_REQUEST,sr.getId(),"STOCK_REQUEST");
        return new ApiResponse(true,"Request submitted successfully",toDTO(sr));
    }

    public ApiResponse process(Long id, ProcessRequestDTO dto, String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        StockRequest sr = reqRepo.findById(id).orElse(null);
        if (sr==null||!sr.getAdmin().getId().equals(admin.getId()))
            return new ApiResponse(false,"Request not found",null);
        if (sr.getStatus()!=StockRequest.RequestStatus.PENDING)
            return new ApiResponse(false,"Request already processed",null);
        StockRequest.RequestStatus status;
        try { status = StockRequest.RequestStatus.valueOf(dto.getAction().toUpperCase()); }
        catch (Exception e) { return new ApiResponse(false,"Action must be APPROVED or REJECTED",null); }
        sr.setStatus(status); sr.setAdminNotes(dto.getAdminNotes()); sr.setProcessedAt(LocalDateTime.now());
        reqRepo.save(sr);
        if (status==StockRequest.RequestStatus.APPROVED) {
            String txType = sr.getRequestType()==StockRequest.RequestType.STOCK_IN ? "STOCK_IN" : "STOCK_OUT";
            txService.create(new TransactionRequest(sr.getProduct().getId(),txType,sr.getQuantity(),null,
                "Auto-approved for "+sr.getStaff().getFullName()),adminUsername);
        }
        String note = dto.getAdminNotes()!=null&&!dto.getAdminNotes().isBlank() ? ". Note: "+dto.getAdminNotes() : "";
        notifService.create(sr.getStaff(),
            status==StockRequest.RequestStatus.APPROVED ? "Request Approved" : "Request Rejected",
            "Your request for '"+sr.getProduct().getName()+"' was "+status.name().toLowerCase()+note,
            Notification.NotificationType.REQUEST_UPDATE,sr.getId(),"STOCK_REQUEST");
        return new ApiResponse(true,"Request "+status.name().toLowerCase(),toDTO(sr));
    }

    @Transactional(readOnly=true)
    public ApiResponse getForAdmin(String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        return new ApiResponse(true,"OK",reqRepo.findByAdminIdOrderByCreatedAtDesc(admin.getId()).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly=true)
    public ApiResponse getPendingForAdmin(String adminUsername) {
        User admin = userRepo.findByUsername(adminUsername).orElse(null);
        if (admin==null) return new ApiResponse(false,"Admin not found",null);
        return new ApiResponse(true,"OK",reqRepo.findByAdminIdAndStatusOrderByCreatedAtDesc(admin.getId(),StockRequest.RequestStatus.PENDING).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly=true)
    public ApiResponse getForStaff(String staffUsername) {
        User staff = userRepo.findByUsername(staffUsername).orElse(null);
        if (staff==null) return new ApiResponse(false,"Staff not found",null);
        return new ApiResponse(true,"OK",reqRepo.findByStaffIdOrderByCreatedAtDesc(staff.getId()).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly=true)
    public long pendingCount(Long adminId) {
        return reqRepo.countByAdminIdAndStatus(adminId,StockRequest.RequestStatus.PENDING);
    }

    private StockRequestResponse toDTO(StockRequest sr) {
        StockRequestResponse r = new StockRequestResponse();
        r.setId(sr.getId()); r.setProductName(sr.getProduct().getName()); r.setProductId(sr.getProduct().getId());
        r.setStaffName(sr.getStaff().getFullName()); r.setStaffUsername(sr.getStaff().getUsername());
        r.setRequestType(sr.getRequestType().name()); r.setQuantity(sr.getQuantity());
        r.setReason(sr.getReason()); r.setStatus(sr.getStatus().name());
        r.setAdminNotes(sr.getAdminNotes()); r.setCreatedAt(sr.getCreatedAt()); r.setProcessedAt(sr.getProcessedAt());
        return r;
    }
}
