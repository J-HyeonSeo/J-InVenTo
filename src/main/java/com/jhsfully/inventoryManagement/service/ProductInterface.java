package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.ProductDto;

import java.util.List;

public interface ProductInterface {

    public ProductDto.ProductResponse getProduct(Long id);
    public List<ProductDto.ProductResponse> getAllProducts();
    public List<ProductDto.ProductResponse> getProducts();

    public ProductDto.ProductResponse addProduct(ProductDto.ProductAddRequest request);

    public ProductDto.ProductResponse updateProduct(ProductDto.ProductUpdateRequest request);

    public void disableProduct(Long id);
    public void deleteProduct(Long id);

}
