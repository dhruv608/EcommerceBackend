package com.store.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.store.backend.entity.Product;

public interface ProductService {
	Product createProduct(Product product, List<MultipartFile> images);

	Page<Product> getFilteredProducts(String search, Long categoryId, Boolean isActive, Boolean isFeatured, int page,
			int size, String sortBy, String direction  , Integer minPrice,
	        Integer maxPrice);

	List<Product> getProductsByCategory(Long categoryId);

	Product getProductById(Long id);

	Product updateProduct(Long id, Product updatedProduct, List<MultipartFile> images);

	void deleteProduct(Long id);

	public Product deleteProductImage(Long productId, String imageUrl);

	Product updatePartial(Long id, Map<String, Object> updates);

}
