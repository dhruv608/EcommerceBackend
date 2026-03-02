package com.store.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.backend.entity.Category;
import com.store.backend.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		super();
		this.categoryService = categoryService;
	}

	@PostMapping
	public ResponseEntity<Category> create(@Valid @RequestBody Category category) {
		Category saved = categoryService.createCategory(category);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> getAll() {
		List<Map<String, Object>> categories = categoryService.getAllCategoriesWithCount();
		return ResponseEntity.ok(categories);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category) {
		Category updated = categoryService.updateCategory(id, category);
		return ResponseEntity.ok(updated);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {

	    categoryService.deleteCategory(id);

	    return ResponseEntity.noContent().build();
	}


}
