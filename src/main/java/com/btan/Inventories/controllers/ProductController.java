package com.btan.Inventories.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.btan.Inventories.Util.ProductDTO;
import com.btan.Inventories.model.Category;
import com.btan.Inventories.model.Product;
import com.btan.Inventories.model.Supplier;
import com.btan.Inventories.service.CategoryService;
import com.btan.Inventories.service.ProductService;
import com.btan.Inventories.service.SupplierService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @Value("${product.picture.upload.directory}")
    private String uploadDirectory;

    @PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute ProductDTO productDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());

            // find category and add it
            Category cat = null;
            Supplier supplier = null;
            if (categoryService.getCategory(productDTO.getCategoryId()).isPresent()
                    && supplierService.getSupplier(productDTO.getSupplierId()).isPresent()) {
                cat = categoryService.getCategory(productDTO.getCategoryId()).get();
                supplier = supplierService.getSupplier(productDTO.getSupplierId()).get();
            } else {
                response.put("Error", "Category not found");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            product.setCategory(cat);
            product.setSupplier(supplier);
            product.setAvailableQuantinty(productDTO.getAvailableQuantinty());
            product.setUnitPrice(productDTO.getUnitPrice());

            String pictureName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    + "_" + productDTO.getProductImage().getOriginalFilename();

            // Create the upload directory if it doesn't exist
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            product.setProductImage(pictureName);
            // Save the file to the specified directory
            String filePath = uploadDirectory + pictureName;
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(productDTO.getProductImage().getBytes());
            fos.close();

            response = productService.addProduct(product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{productId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getProduct(@PathVariable UUID productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            ;
            if (productService.getProduct(productId).isEmpty()) {
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("product", productService.getProduct(productId).get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        try {
            response = productService.getAllProducts();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{productId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateProduct(@PathVariable UUID productId, @ModelAttribute ProductDTO productDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Product> existingProductOptional = productService.getProduct(productId);
            if (existingProductOptional.isEmpty()) {
                response.put("Error", "Product not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Product existingProduct = existingProductOptional.get();
            updateProductFromDTO(existingProduct, productDTO);

            if (productDTO.getProductImage() != null && !productDTO.getProductImage().isEmpty()) {

                String pictureName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_"
                        + productDTO.getProductImage().getOriginalFilename();
                File directory = new File(uploadDirectory);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                existingProduct.setProductImage(pictureName);

                // Save the file to the specified directory
                String filePath = uploadDirectory + pictureName;
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(productDTO.getProductImage().getBytes());
                }
            }
            // Create the upload directory if it doesn't exist

            response = productService.updateProduct(productId, existingProduct);
            if (response.containsKey("Error")) {
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{productId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> removeProduct(@PathVariable UUID productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = productService.removeProduct(productId);
            if (response.containsKey("Error")) {
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void updateProductFromDTO(Product product, ProductDTO productDTO) throws Exception {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setAvailableQuantinty(productDTO.getAvailableQuantinty());
        product.setUnitPrice(productDTO.getUnitPrice());

        // Handle MultipartFile conversion here
        MultipartFile file = productDTO.getProductImage();
        if (file != null) {
            product.setProductImage(file.getOriginalFilename());
        }
    }
}
