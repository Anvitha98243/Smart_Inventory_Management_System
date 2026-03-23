package com.inventory.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="product_id", nullable=false) private Product product;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private TransactionType type;
    @Column(nullable=false) private Integer quantity;
    @Column(precision=10, scale=2) private BigDecimal unitPrice;
    @Column(precision=10, scale=2) private BigDecimal totalAmount;
    @Column(length=500) private String notes;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="performed_by", nullable=false) private User performedBy;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="admin_id", nullable=false) private User admin;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    private Integer quantityBefore;
    private Integer quantityAfter;

    public enum TransactionType { PURCHASE, SALE, ADJUSTMENT, STOCK_IN, STOCK_OUT }

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Product getProduct(){return product;} public void setProduct(Product v){product=v;}
    public TransactionType getType(){return type;} public void setType(TransactionType v){type=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal v){unitPrice=v;}
    public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal v){totalAmount=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public User getPerformedBy(){return performedBy;} public void setPerformedBy(User v){performedBy=v;}
    public User getAdmin(){return admin;} public void setAdmin(User v){admin=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    public Integer getQuantityBefore(){return quantityBefore;} public void setQuantityBefore(Integer v){quantityBefore=v;}
    public Integer getQuantityAfter(){return quantityAfter;} public void setQuantityAfter(Integer v){quantityAfter=v;}
}
