package com.store.backend.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.store.backend.entity.Product;
import com.store.backend.service.ProductService;

import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;

	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Product> create(@Valid 
			@RequestPart("product") String productJson,
			@RequestPart("images") MultipartFile[] images) throws Exception {

		System.out.println("HIT"); 

		ObjectMapper mapper = new ObjectMapper();
		Product product = mapper.readValue(productJson, Product.class);

		Product saved = productService.createProduct(product, Arrays.asList(images));

		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@GetMapping
	public ResponseEntity<Page<Product>> getProducts(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Long categoryId, 
			@RequestParam(required = false) Boolean isActive,
			@RequestParam(required = false) Boolean isFeatured,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "createdAt") String sortBy,
	        @RequestParam(defaultValue = "desc") String direction,
	        @RequestParam(required = false) Integer minPrice,
	        @RequestParam(required = false) Integer maxPrice
			) {
		return ResponseEntity.ok(productService.getFilteredProducts(search, categoryId, isActive,
				isFeatured,   page, size, sortBy, direction ,   minPrice,
	            maxPrice));
	}

	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<Product>> byCategory(@PathVariable Long categoryId) {

		return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {

		Product product = productService.getProductById(id);
		return ResponseEntity.ok(product);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Product> updateProductById(
			@PathVariable Long id,
			@RequestPart("product") String productJson,
			@RequestPart(value = "images", required = false) MultipartFile[] images) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Product updatedProduct = mapper.readValue(productJson, Product.class);
		Product saved = productService.updateProduct(id, updatedProduct, images == null ? null : Arrays.asList(images));
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProductId(@PathVariable Long id) {
		productService.deleteProduct(id);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{productId}/images")
	public ResponseEntity<Product> deleteImage(@PathVariable Long productId, @RequestParam String imageUrl) {
		Product updated = productService.deleteProductImage(productId, imageUrl);
		return ResponseEntity.ok(updated);
	}

	@PatchMapping("/{id}")
	public Product updateFlags(@PathVariable Long id, @RequestBody Map<String, Object> updates) {

		return productService.updatePartial(id, updates);
	}



}
