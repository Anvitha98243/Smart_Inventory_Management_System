package com.inventory.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public class ProductRequest {
    @NotBlank public String name;
    public String description;
    @NotBlank public String category;
    public String sku;
    @NotNull @Min(0) public Integer quantity;
    @NotNull @Min(0) public Integer minThreshold;
    @NotNull public BigDecimal price;
    public String unit;
    public String getName(){ return name; }
    public String getDescription(){ return description; }
    public String getCategory(){ return category; }
    public String getSku(){ return sku; }
    public Integer getQuantity(){ return quantity; }
    public Integer getMinThreshold(){ return minThreshold; }
    public BigDecimal getPrice(){ return price; }
    public String getUnit(){ return unit; }
}
