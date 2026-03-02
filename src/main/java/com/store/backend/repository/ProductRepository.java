package com.store.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.store.backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategoryId(Long categoryId);

	List<Product> findByIsActiveTrue();

	@Query("SELECT p FROM Product p " +
			"WHERE (:search IS NULL OR :search = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
			"AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
			"AND (:isActive IS NULL OR p.isActive = :isActive) " +
			"AND (:isFeatured IS NULL OR p.isFeatured = :isFeatured) " +
			"AND (:minPrice IS NULL OR p.price >= :minPrice) " +
			"AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
	Page<Product> findFilteredProducts(@Param("search") String search, @Param("categoryId") Long categoryId,
			@Param("isActive") Boolean isActive, @Param("isFeatured") Boolean isFeatured, Pageable pageable,
			@Param("minPrice") Integer minPrice,
			@Param("maxPrice") Integer maxPrice);

	long countByCategoryId(Long categoryId);

}
