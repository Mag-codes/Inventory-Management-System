package com.btan.Inventories.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String name;

    @NotNull
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Transient
    @JsonProperty("categoryName")
    private String categoryName;

    @NotNull
    @Column(name = "product_image")
    private String productImage;

    @NotNull
    @Column(name = "unit_price", nullable = false)
    private Float unitPrice;

    @NotNull
    @Column(name = "available_quantity")
    private Double availableQuantinty;

    @NotNull
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Transient
    @JsonProperty("categoryName")
    private String supplierName;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @PostLoad
    private void postLoad() {
        if (category != null) {
            this.categoryName = category.getName();
        }
        if (supplier != null) {
            this.supplierName = supplier.getName();
        }
    }

}