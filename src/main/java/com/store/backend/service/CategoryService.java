package com.store.backend.service;

import java.util.List;
import java.util.Map;

import com.store.backend.entity.Category;

public interface CategoryService {

	Category createCategory(Category category);

	List<Map<String,Object>> getAllCategoriesWithCount();

	void deleteCategory(Long id);

	Category updateCategory(Long id, Category catgeory);

}
