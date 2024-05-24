package com.btan.Inventories.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btan.Inventories.model.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}