package com.store.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.backend.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	
	List<Cart> findByUserId(Long userId);
	
	Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
	
	@Modifying
	@Query("DELETE FROM Cart c WHERE c.user.id = :userId AND c.product.id = :productId")
	void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
	
	@Modifying
	@Query("DELETE FROM Cart c WHERE c.user.id = :userId")
	void deleteByUserId(@Param("userId") Long userId);
	
	@Query("SELECT COUNT(c) FROM Cart c WHERE c.user.id = :userId")
	Long countByUserId(@Param("userId") Long userId);
	
	@Query("SELECT COALESCE(SUM(c.quantity), 0) FROM Cart c WHERE c.user.id = :userId")
	Long getTotalItemsByUserId(@Param("userId") Long userId);
}
