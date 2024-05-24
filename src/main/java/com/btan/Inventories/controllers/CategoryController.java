package com.btan.Inventories.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.btan.Inventories.model.Category;
import com.btan.Inventories.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> addCategory(@Valid @RequestBody Category category) {
        Map<String, Object> response = categoryService.addCategory(category);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getCategory(@PathVariable UUID categoryId) {
        Optional<Category> optionalCategory = categoryService.getCategory(categoryId);

        Map<String, Object> response = new HashMap<>();

        if (optionalCategory.isPresent()) {
            response.put("data", optionalCategory.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        Map<String, Object> response = new HashMap<>();
        response.put("categories", categories);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable UUID categoryId,
            @Valid @RequestBody Category updatedCategory) {
        Map<String, Object> response = categoryService.updateCategory(categoryId, updatedCategory);
        if (response.containsKey("Error")) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> removeCategory(@PathVariable UUID categoryId) {
        Map<String, Object> response = categoryService.removeCategory(categoryId);
        if (response.containsKey("Error")) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
