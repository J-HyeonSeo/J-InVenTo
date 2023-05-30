package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.model.ProductEntity;

import java.util.List;

public interface ProductInterface {

    public List<ProductDto.ProductResponse> getProducts();

    public ProductDto.ProductResponse addProduct(ProductDto.ProductAddRequest request);

    public ProductDto.ProductResponse updateProduct(ProductDto.ProductUpdateRequest request);

    public void deleteProduct(Long id);

}
