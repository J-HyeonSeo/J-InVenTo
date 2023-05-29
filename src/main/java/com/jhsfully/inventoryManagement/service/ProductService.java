package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public List<ProductEntity> getProduct(){
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities;
    }


}
