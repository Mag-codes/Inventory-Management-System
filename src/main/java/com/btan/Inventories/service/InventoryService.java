package com.btan.Inventories.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.btan.Inventories.model.Inventory;
import com.btan.Inventories.repo.InventoryRepository;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Map<String, Object> addInventory(@Valid Inventory inventory) {
        Map<String, Object> response = new HashMap<>();
        Inventory savedInventory = inventoryRepository.save(inventory);
        response.put("message", "Inventory successfully added");
        response.put("data", savedInventory);
        return response;
    }

    public Optional<Inventory> getInventory(UUID inventoryId) {
        return inventoryRepository.findById(inventoryId);
    }

    public Map<String, Object> getAllInventories() {
        Map<String, Object> response = new HashMap<>();
        List<Inventory> inventories = inventoryRepository.findAll();
        response.put("data", inventories);
        return response;
    }

    public Map<String, Object> updateInventory(UUID inventoryId, @Valid Inventory updatedInventory) {
        Map<String, Object> response = new HashMap<>();
        Optional<Inventory> optionalInventory = inventoryRepository.findById(inventoryId);
        if (optionalInventory.isPresent()) {
            Inventory existingInventory = optionalInventory.get();

            // existingInventory.setProductId(updatedInventory.getProductId());
            existingInventory.setLocation(updatedInventory.getLocation());
            existingInventory.setQuantity(updatedInventory.getQuantity());

            inventoryRepository.save(existingInventory);
            response.put("message", "Inventory updated successfully");
            response.put("data", existingInventory);
        } else {
            response.put("Error", "Inventory not found");
        }
        return response;
    }

    public Map<String, Object> removeInventory(UUID inventoryId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Inventory> optionalInventory = inventoryRepository.findById(inventoryId);
        if (optionalInventory.isPresent()) {
            inventoryRepository.delete(optionalInventory.get());
            response.put("message", "Inventory removed successfully");
        } else {
            response.put("Error", "Inventory not found");
        }
        return response;
    }
}
