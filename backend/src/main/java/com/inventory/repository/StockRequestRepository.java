package com.inventory.repository;
import com.inventory.entity.StockRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface StockRequestRepository extends JpaRepository<StockRequest,Long> {
    List<StockRequest> findByAdminIdOrderByCreatedAtDesc(Long adminId);
    List<StockRequest> findByStaffIdOrderByCreatedAtDesc(Long staffId);
    List<StockRequest> findByAdminIdAndStatusOrderByCreatedAtDesc(Long adminId, StockRequest.RequestStatus status);
    long countByAdminIdAndStatus(Long adminId, StockRequest.RequestStatus status);
}
