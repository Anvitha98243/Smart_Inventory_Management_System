package com.inventory.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class TransactionResponse {
    public Long id, productId; public String productName, type, notes, performedBy;
    public Integer quantity, quantityBefore, quantityAfter;
    public BigDecimal unitPrice, totalAmount; public LocalDateTime createdAt;
    public Long getId(){ return id; } public void setId(Long v){ id=v; }
    public String getProductName(){ return productName; } public void setProductName(String v){ productName=v; }
    public Long getProductId(){ return productId; } public void setProductId(Long v){ productId=v; }
    public String getType(){ return type; } public void setType(String v){ type=v; }
    public Integer getQuantity(){ return quantity; } public void setQuantity(Integer v){ quantity=v; }
    public BigDecimal getUnitPrice(){ return unitPrice; } public void setUnitPrice(BigDecimal v){ unitPrice=v; }
    public BigDecimal getTotalAmount(){ return totalAmount; } public void setTotalAmount(BigDecimal v){ totalAmount=v; }
    public String getNotes(){ return notes; } public void setNotes(String v){ notes=v; }
    public String getPerformedBy(){ return performedBy; } public void setPerformedBy(String v){ performedBy=v; }
    public Integer getQuantityBefore(){ return quantityBefore; } public void setQuantityBefore(Integer v){ quantityBefore=v; }
    public Integer getQuantityAfter(){ return quantityAfter; } public void setQuantityAfter(Integer v){ quantityAfter=v; }
    public LocalDateTime getCreatedAt(){ return createdAt; } public void setCreatedAt(LocalDateTime v){ createdAt=v; }
}
