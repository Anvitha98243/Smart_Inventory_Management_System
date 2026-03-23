package com.inventory.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public class TransactionRequest {
    @NotNull public Long productId;
    @NotBlank public String type;
    @NotNull @Min(1) public Integer quantity;
    public BigDecimal unitPrice;
    public String notes;
    public TransactionRequest(){}
    public TransactionRequest(Long pid, String type, Integer qty, BigDecimal price, String notes){
        this.productId=pid; this.type=type; this.quantity=qty; this.unitPrice=price; this.notes=notes;
    }
    public Long getProductId(){ return productId; }
    public String getType(){ return type; }
    public Integer getQuantity(){ return quantity; }
    public BigDecimal getUnitPrice(){ return unitPrice; }
    public String getNotes(){ return notes; }
}
