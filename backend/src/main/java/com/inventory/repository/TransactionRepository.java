package com.inventory.repository;
import com.inventory.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByAdminIdOrderByCreatedAtDesc(Long adminId);
    List<Transaction> findByProductIdOrderByCreatedAtDesc(Long productId);
    @Query("SELECT t FROM Transaction t WHERE t.admin.id=:aid AND t.createdAt BETWEEN :s AND :e ORDER BY t.createdAt DESC")
    List<Transaction> findByRange(@Param("aid") Long adminId, @Param("s") LocalDateTime s, @Param("e") LocalDateTime e);
    @Query("SELECT COALESCE(SUM(t.totalAmount),0) FROM Transaction t WHERE t.admin.id=:aid AND t.type='SALE' AND t.createdAt BETWEEN :s AND :e")
    BigDecimal sumSales(@Param("aid") Long adminId, @Param("s") LocalDateTime s, @Param("e") LocalDateTime e);
    @Query("SELECT COALESCE(SUM(t.totalAmount),0) FROM Transaction t WHERE t.admin.id=:aid AND t.type='PURCHASE' AND t.createdAt BETWEEN :s AND :e")
    BigDecimal sumPurchases(@Param("aid") Long adminId, @Param("s") LocalDateTime s, @Param("e") LocalDateTime e);
}
