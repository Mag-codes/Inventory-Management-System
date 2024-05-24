package com.btan.Inventories.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.btan.Inventories.model.Supplier;
import com.btan.Inventories.repo.SupplierRepository;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public Map<String, Object> addSupplier(@Valid Supplier supplier) {
        Map<String, Object> response = new HashMap<>();
        Supplier savedSupplier = supplierRepository.save(supplier);
        response.put("message", "Supplier successfully added");
        response.put("data", savedSupplier);
        return response;
    }

    public Optional<Supplier> getSupplier(UUID supplierId) {
        return supplierRepository.findById(supplierId);
    }

    public Map<String, Object> getAllSuppliers() {
        Map<String, Object> response = new HashMap<>();
        List<Supplier> suppliers = supplierRepository.findAll();
        response.put("data", suppliers);
        return response;
    }

    public Map<String, Object> updateSupplier(UUID supplierId, @Valid Supplier updatedSupplier) {
        Map<String, Object> response = new HashMap<>();
        Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierId);
        if (optionalSupplier.isPresent()) {
            Supplier existingSupplier = optionalSupplier.get();

            if (updatedSupplier.getName() != null) {
                existingSupplier.setName(updatedSupplier.getName());
            }
            if (updatedSupplier.getCompanyInfo() != null) {
                existingSupplier.setCompanyInfo(updatedSupplier.getCompanyInfo());
            }
            if (updatedSupplier.getAddress() != null) {
                existingSupplier.setAddress(updatedSupplier.getAddress());
            }
            if (updatedSupplier.getEmail() != null) {
                existingSupplier.setEmail(updatedSupplier.getEmail());
            }
            if (updatedSupplier.getPhone() != null) {
                existingSupplier.setPhone(updatedSupplier.getPhone());
            }

            supplierRepository.save(existingSupplier);
            response.put("message", "Supplier updated successfully");
            response.put("data", existingSupplier);
        } else {
            response.put("Error", "Supplier not found");
        }
        return response;
    }

    public Map<String, Object> removeSupplier(UUID supplierId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierId);
        if (optionalSupplier.isPresent()) {
            supplierRepository.delete(optionalSupplier.get());
            response.put("message", "Supplier removed successfully");
        } else {
            response.put("Error", "Supplier not found");
        }
        return response;
    }
}
