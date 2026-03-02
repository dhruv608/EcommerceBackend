package com.store.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.store.backend.entity.Category;
import com.store.backend.entity.Product;
import com.store.backend.repository.CategoryRepository;
import com.store.backend.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ImageUploadService imageUploadService;

	public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
			ImageUploadService imageUploadService) {
		super();
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.imageUploadService = imageUploadService;
	}

	@Override
	public Product createProduct(Product product, List<MultipartFile> images) {
		Long categoryId = product.getCategory().getId();

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category Not Found"));

		product.setCategory(category);
		List<String> imageUrls = new ArrayList<>();

		for (MultipartFile file : images) {
			String url = imageUploadService.uploadImage(file);
			imageUrls.add(url);
		}
		product.setImages(imageUrls);
		return productRepository.save(product);
	}

	@Override
	public List<Product> getProductsByCategory(Long categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	}

	@Override
	public Product updateProduct(Long id, Product updatedProduct, List<MultipartFile> images) {

		Product existing = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

		// 🔹 Update simple fields
		existing.setName(updatedProduct.getName());
		existing.setDescription(updatedProduct.getDescription());
		existing.setPrice(updatedProduct.getPrice());
		existing.setStock(updatedProduct.getStock());
		if (updatedProduct.getIsActive() != null) {
			existing.setIsActive(updatedProduct.getIsActive());
		}
		if (updatedProduct.getIsFeatured() != null) {
			existing.setIsFeatured(updatedProduct.getIsFeatured());
		}
		// 🔹 Update category (if changed)
		if (updatedProduct.getCategory() != null) {
			Category category = categoryRepository.findById(updatedProduct.getCategory().getId())
					.orElseThrow(() -> new RuntimeException("Category not found"));
			existing.setCategory(category);
		}

		// 🔹 Add new images (KEEP old images)
		if (images != null && !images.isEmpty()) {
			List<String> imageUrls = existing.getImages();

			for (MultipartFile file : images) {
				String url = imageUploadService.uploadImage(file);
				imageUrls.add(url);
			}

			existing.setImages(imageUrls);
		}

		return productRepository.save(existing);
	}

	@Override
	public void deleteProduct(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not Found"));
		product.setIsActive(false);
		productRepository.delete(product);
	}

	@Override
	public Product deleteProductImage(Long productId, String imageUrl) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		if (!product.getImages().contains(imageUrl)) {
			throw new RuntimeException("Image not found in product");
		}

		imageUploadService.deleteImage(imageUrl);
		product.getImages().remove(imageUrl);
		return productRepository.save(product);
	}

	@Override
	public Product updatePartial(Long id, Map<String, Object> updates) {

		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

		if (updates.containsKey("isActive")) {
			Boolean isActive = (Boolean) updates.get("isActive");
			product.setIsActive(isActive);
		}

		if (updates.containsKey("isFeatured")) {
			Boolean isFeatured = (Boolean) updates.get("isFeatured");
			product.setIsFeatured(isFeatured);
		}

		return productRepository.save(product);
	}

	@Override
	public Page<Product> getFilteredProducts(String search, Long categoryId, Boolean isActive, 
			Boolean isFeatured ,
	        int page,
	        int size,
	        String sortBy,
	        String direction,  Integer minPrice,
	        Integer maxPrice) {
		 Sort sort = direction.equalsIgnoreCase("desc")
		            ? Sort.by(sortBy).descending()
		            : Sort.by(sortBy).ascending();

		    Pageable pageable = PageRequest.of(page, size, sort);
		
		return productRepository.findFilteredProducts(search, categoryId, isActive, isFeatured ,  pageable ,   minPrice,
	            maxPrice);
	}

}
