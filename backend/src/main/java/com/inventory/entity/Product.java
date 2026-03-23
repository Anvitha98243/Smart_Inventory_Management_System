package com.inventory.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=200) private String name;
    @Column(length=1000) private String description;
    @Column(nullable=false, length=100) private String category;
    @Column(length=100) private String sku;
    @Column(nullable=false) private Integer quantity = 0;
    @Column(nullable=false) private Integer minThreshold = 10;
    @Column(nullable=false, precision=10, scale=2) private BigDecimal price = BigDecimal.ZERO;
    @Column(length=50) private String unit;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="admin_id", nullable=false) private User admin;
    @Column(nullable=false) private boolean active = true;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @PreUpdate public void preUpdate(){ updatedAt = LocalDateTime.now(); }
    public boolean isBelowThreshold(){ return quantity!=null && minThreshold!=null && quantity<=minThreshold; }

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getSku(){return sku;} public void setSku(String v){sku=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public Integer getMinThreshold(){return minThreshold;} public void setMinThreshold(Integer v){minThreshold=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public String getUnit(){return unit;} public void setUnit(String v){unit=v;}
    public User getAdmin(){return admin;} public void setAdmin(User v){admin=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime v){updatedAt=v;}
}
