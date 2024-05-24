package com.btan.Inventories.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Inventory")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "inventory_id")
    private UUID inventoryId;

    @NotNull
    private Product product;

    @NotBlank
    private String location;

    @NotNull
    private Double quantity;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

}
