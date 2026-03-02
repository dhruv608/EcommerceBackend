package com.store.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.store.backend.entity.Category;
import com.store.backend.exception.CategoryDeleteException;
import com.store.backend.repository.CategoryRepository;
import com.store.backend.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
		super();
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@Override
	public Category createCategory(Category category) {

		if (categoryRepository.existsByName(category.getName())) {
			throw new RuntimeException("Category already exists");
		}
		return categoryRepository.save(category);

	}

	@Override
	public void deleteCategory(Long id) {
		long count = productRepository.countByCategoryId(id);

	    if (count > 0) {
	        throw new CategoryDeleteException(
	            "Category has products. Remove products first."
	        );
	    }

	    categoryRepository.deleteById(id);
		

	}

	@Override
	public Category updateCategory(Long id, Category category) {
		Category existing = categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		if (category.getName() != null) {
			existing.setName(category.getName());
		}

		if (category.getDescription() != null) {
			existing.setDescription(category.getDescription());
		}

		if (category.getIsActive() != null) {
			existing.setIsActive(category.getIsActive());
		}

		return categoryRepository.save(existing);
	}

	@Override
	public List<Map<String, Object>> getAllCategoriesWithCount() {
		List<Category> categories = categoryRepository.findAll();
		List<Map<String, Object>> result = new ArrayList<>();
		for (Category category : categories) {
			long count = productRepository.countByCategoryId(category.getId());
			Map<String, Object> map = new HashMap<>();
			map.put("id", category.getId());
			map.put("name", category.getName());
			map.put("description", category.getDescription());
			map.put("isActive", category.getIsActive());
			map.put("productCount", count);
			map.put("createdAt", category.getCreatedAt());
			map.put("updatedAt", category.getUpdatedAt());
			result.add(map);
		}
		return result;
	}

}
