package com.inventory.dto;
import jakarta.validation.constraints.*;
public class StockRequestCreate {
    @NotNull public Long productId;
    @NotBlank public String adminUsername;
    @NotBlank public String requestType;
    @NotNull @Min(1) public Integer quantity;
    public String reason;
    public Long getProductId(){ return productId; }
    public String getAdminUsername(){ return adminUsername; }
    public String getRequestType(){ return requestType; }
    public Integer getQuantity(){ return quantity; }
    public String getReason(){ return reason; }
}
