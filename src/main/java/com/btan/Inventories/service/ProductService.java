package com.btan.Inventories.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.btan.Inventories.model.Product;
import com.btan.Inventories.repo.ProductRepository;

import jakarta.validation.Valid;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Map<String, Object> addProduct(@Valid Product product) {
        Map<String, Object> response = new HashMap<>();
        Product savedProduct = productRepository.save(product);
        response.put("message", "Product successfully added");
        response.put("data", savedProduct);
        return response;
    }

    public Optional<Product> getProduct(UUID productId) {
        return productRepository.findById(productId);
    }

 public Map<String, Object> getAllProducts(int page, int size, String sortBy) {
    Map<String, Object> response = new HashMap<>();
    
    // Create pageable object for pagination and sorting
    PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy));
    
    // Fetch products with pagination and sorting
    Page<Product> productPage = productRepository.findAll(pageable);
    
    response.put("data", productPage.getContent());
    response.put("currentPage", productPage.getNumber());
    response.put("totalItems", productPage.getTotalElements());
    response.put("totalPages", productPage.getTotalPages());
    
    return response;
}

    public Map<String, Object> updateProduct(UUID productId, @Valid Product updatedProduct) {
        Map<String, Object> response = new HashMap<>();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            if (updatedProduct.getName() != null) {
                existingProduct.setName(updatedProduct.getName());
            }
            if (updatedProduct.getCategory() != null) {
                existingProduct.setCategory(updatedProduct.getCategory());
            }
            if (updatedProduct.getProductImage() != null) {
                existingProduct.setProductImage(updatedProduct.getProductImage());
            }
            if (updatedProduct.getUnitPrice() != null) {
                existingProduct.setUnitPrice(updatedProduct.getUnitPrice());
            }
            if (updatedProduct.getAvailableQuantinty() != null) {
                existingProduct.setAvailableQuantinty(updatedProduct.getAvailableQuantinty());
            }
            if (updatedProduct.getDescription() != null) {
                existingProduct.setDescription(updatedProduct.getDescription());
            }

            productRepository.save(existingProduct);
            response.put("message", "Product updated successfully");
            response.put("data", existingProduct);
        } else {
            response.put("Error", "Product not found");
        }
        return response;
    }

    public Map<String, Object> removeProduct(UUID productId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
            response.put("message", "Product removed successfully");
        } else {
            response.put("Error", "Product not found");
        }
        return response;
    }
}
