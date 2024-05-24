package com.btan.Inventories.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Suppliers")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "supplier_id")
    private UUID id;

    @NotBlank
    private String name;

    @Lob
    @Column(name = "company_info")
    private String companyInfo;

    @Lob
    private String address;

    @Email
    private String email;

    @Size(min = 10, max = 10, message = "Phone number must be 10 numbers")
    private String phone;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

}
