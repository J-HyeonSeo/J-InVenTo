package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.ProductDto;

import java.util.List;

public interface ProductInterface {

    ProductDto.ProductResponse getProduct(Long id);
    List<ProductDto.ProductResponse> getAllProducts();
    List<ProductDto.ProductResponse> getProducts();

    ProductDto.ProductResponse addProduct(ProductDto.ProductAddRequest request);

    ProductDto.ProductResponse updateProduct(ProductDto.ProductUpdateRequest request);

    void disableProduct(Long id);
    void deleteProduct(Long id);

}
