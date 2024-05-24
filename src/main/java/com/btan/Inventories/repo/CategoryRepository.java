package com.btan.Inventories.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btan.Inventories.model.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}