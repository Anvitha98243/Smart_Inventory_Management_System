package com.inventory.dto;
import java.time.LocalDateTime;
public class StockRequestResponse {
    public Long id, productId; public String productName, staffName, staffUsername;
    public String requestType, reason, status, adminNotes; public Integer quantity;
    public LocalDateTime createdAt, processedAt;
    public Long getId(){ return id; } public void setId(Long v){ id=v; }
    public String getProductName(){ return productName; } public void setProductName(String v){ productName=v; }
    public Long getProductId(){ return productId; } public void setProductId(Long v){ productId=v; }
    public String getStaffName(){ return staffName; } public void setStaffName(String v){ staffName=v; }
    public String getStaffUsername(){ return staffUsername; } public void setStaffUsername(String v){ staffUsername=v; }
    public String getRequestType(){ return requestType; } public void setRequestType(String v){ requestType=v; }
    public Integer getQuantity(){ return quantity; } public void setQuantity(Integer v){ quantity=v; }
    public String getReason(){ return reason; } public void setReason(String v){ reason=v; }
    public String getStatus(){ return status; } public void setStatus(String v){ status=v; }
    public String getAdminNotes(){ return adminNotes; } public void setAdminNotes(String v){ adminNotes=v; }
    public LocalDateTime getCreatedAt(){ return createdAt; } public void setCreatedAt(LocalDateTime v){ createdAt=v; }
    public LocalDateTime getProcessedAt(){ return processedAt; } public void setProcessedAt(LocalDateTime v){ processedAt=v; }
}
