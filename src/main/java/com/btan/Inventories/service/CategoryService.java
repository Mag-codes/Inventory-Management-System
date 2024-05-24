package com.btan.Inventories.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btan.Inventories.model.Category;
import com.btan.Inventories.repo.CategoryRepository;

import jakarta.validation.Valid;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Map<String, Object> addCategory(@Valid Category category) {
        Map<String, Object> response = new HashMap<>();
        Category savedCategory = categoryRepository.save(category);
        response.put("message", "Category successfully added");
        response.put("data", savedCategory);
        return response;
    }

    public Optional<Category> getCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    public Map<String, Object> updateCategory(UUID categoryId, @Valid Category updatedCategory) {
        Map<String, Object> response = new HashMap<>();
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();

            if (updatedCategory.getName() != null) {
                existingCategory.setName(updatedCategory.getName());
            }
            if (updatedCategory.getDescription() != null) {
                existingCategory.setDescription(updatedCategory.getDescription());
            }
            existingCategory.setUpatedAt(LocalDateTime.now());

            categoryRepository.save(existingCategory);
            response.put("message", "Category updated successfully");
            response.put("data", existingCategory);
        } else {
            response.put("Error", "Category not found");
        }
        return response;
    }

    public Map<String, Object> removeCategory(UUID categoryId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            categoryRepository.delete(optionalCategory.get());
            response.put("message", "Category removed successfully");
        } else {
            response.put("Error", "Category not found");
        }
        return response;
    }
}
