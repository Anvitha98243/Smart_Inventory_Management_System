package com.inventory.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "stock_requests")
public class StockRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="product_id", nullable=false) private Product product;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="staff_id", nullable=false) private User staff;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="admin_id", nullable=false) private User admin;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private RequestType requestType;
    @Column(nullable=false) private Integer quantity;
    @Column(length=500) private String reason;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private RequestStatus status = RequestStatus.PENDING;
    @Column(length=500) private String adminNotes;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime processedAt;

    public enum RequestType { STOCK_IN, STOCK_OUT }
    public enum RequestStatus { PENDING, APPROVED, REJECTED }

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Product getProduct(){return product;} public void setProduct(Product v){product=v;}
    public User getStaff(){return staff;} public void setStaff(User v){staff=v;}
    public User getAdmin(){return admin;} public void setAdmin(User v){admin=v;}
    public RequestType getRequestType(){return requestType;} public void setRequestType(RequestType v){requestType=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public String getReason(){return reason;} public void setReason(String v){reason=v;}
    public RequestStatus getStatus(){return status;} public void setStatus(RequestStatus v){status=v;}
    public String getAdminNotes(){return adminNotes;} public void setAdminNotes(String v){adminNotes=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    public LocalDateTime getProcessedAt(){return processedAt;} public void setProcessedAt(LocalDateTime v){processedAt=v;}
}
