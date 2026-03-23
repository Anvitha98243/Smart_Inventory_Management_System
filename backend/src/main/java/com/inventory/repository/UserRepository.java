package com.inventory.repository;
import com.inventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String u);
    Optional<User> findByEmail(String e);
    Optional<User> findByResetToken(String t);
    boolean existsByUsername(String u);
    boolean existsByEmail(String e);
}
