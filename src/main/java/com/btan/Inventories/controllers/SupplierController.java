package com.btan.Inventories.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.btan.Inventories.model.Supplier;
import com.btan.Inventories.service.SupplierService;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/add")
    public ResponseEntity<?> addSupplier(@Valid @RequestBody Supplier supplier) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = supplierService.addSupplier(supplier);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<?> getSupplier(@PathVariable UUID supplierId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Supplier> supplier = supplierService.getSupplier(supplierId);
            if (supplier.isEmpty()) {
                response.put("Error", "Supplier not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("supplier", supplier.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSuppliers() {
        Map<String, Object> response = new HashMap<>();
        try {
            response = supplierService.getAllSuppliers();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<?> updateSupplier(@PathVariable UUID supplierId, @Valid @RequestBody Supplier supplier) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = supplierService.updateSupplier(supplierId, supplier);
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

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<?> removeSupplier(@PathVariable UUID supplierId) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = supplierService.removeSupplier(supplierId);
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
}
