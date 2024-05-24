package com.btan.Inventories.Util;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductDTO {

    private String name;

    private UUID categoryId;
    
    private UUID supplierId;

    private MultipartFile productImage;

    private Float unitPrice;

    private Double availableQuantinty;

    private String description;
}
