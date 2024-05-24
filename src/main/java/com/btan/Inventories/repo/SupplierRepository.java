package com.btan.Inventories.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.btan.Inventories.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
}
