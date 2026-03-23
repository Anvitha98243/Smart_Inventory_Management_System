package com.inventory.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class ProductResponse {
    public Long id; public String name, description, category, sku, unit;
    public Integer quantity, minThreshold; public BigDecimal price;
    public boolean lowStock; public LocalDateTime createdAt, updatedAt;
    public Long getId(){ return id; } public void setId(Long v){ id=v; }
    public String getName(){ return name; } public void setName(String v){ name=v; }
    public String getDescription(){ return description; } public void setDescription(String v){ description=v; }
    public String getCategory(){ return category; } public void setCategory(String v){ category=v; }
    public String getSku(){ return sku; } public void setSku(String v){ sku=v; }
    public Integer getQuantity(){ return quantity; } public void setQuantity(Integer v){ quantity=v; }
    public Integer getMinThreshold(){ return minThreshold; } public void setMinThreshold(Integer v){ minThreshold=v; }
    public BigDecimal getPrice(){ return price; } public void setPrice(BigDecimal v){ price=v; }
    public String getUnit(){ return unit; } public void setUnit(String v){ unit=v; }
    public boolean isLowStock(){ return lowStock; } public void setLowStock(boolean v){ lowStock=v; }
    public LocalDateTime getCreatedAt(){ return createdAt; } public void setCreatedAt(LocalDateTime v){ createdAt=v; }
    public LocalDateTime getUpdatedAt(){ return updatedAt; } public void setUpdatedAt(LocalDateTime v){ updatedAt=v; }
}
