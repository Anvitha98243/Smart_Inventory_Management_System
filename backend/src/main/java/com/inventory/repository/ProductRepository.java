package com.inventory.repository;
import com.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByAdminIdAndActiveTrue(Long adminId);
    boolean existsBySkuAndAdminId(String sku, Long adminId);
    @Query("SELECT p FROM Product p WHERE p.admin.id=:aid AND p.active=true AND p.quantity<=p.minThreshold")
    List<Product> findLowStock(@Param("aid") Long adminId);
    @Query("SELECT p FROM Product p WHERE p.admin.id=:aid AND p.active=true AND (LOWER(p.name) LIKE LOWER(CONCAT('%',:kw,'%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%',:kw,'%')))")
    List<Product> search(@Param("aid") Long adminId, @Param("kw") String kw);
}
