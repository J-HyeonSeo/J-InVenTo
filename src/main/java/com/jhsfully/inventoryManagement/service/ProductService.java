package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.exception.BomException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.repository.BomRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.type.BomErrorType;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.ProductErrorType.*;

@Service
@AllArgsConstructor
@Transactional
public class ProductService implements ProductInterface{

    private final ProductRepository productRepository;
    private final BomRepository bomRepository;

    @Override
    public List<ProductDto.ProductResponse> getProducts(){
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream()
                .map(ProductEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto.ProductResponse addProduct(ProductDto.ProductAddRequest request) {

        validateAddProduct(request);

        ProductEntity productEntity = ProductEntity.builder()
                .name(request.getName())
                .company(request.getCompany())
                .price(request.getPrice())
                .spec(request.getSpec())
                .build();

        return ProductEntity.toDto(productRepository.save(productEntity));
    }

    @Override
    public ProductDto.ProductResponse updateProduct(ProductDto.ProductUpdateRequest request) {
        ProductEntity productEntity = productRepository.findById(request.getId())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if(request.getName() != null && !request.getName().trim().equals(""))
            productEntity.setName(request.getName());
        if(request.getCompany() != null && !request.getCompany().trim().equals(""))
            productEntity.setCompany(request.getCompany());
        if(request.getPrice() >= 0) {
            productEntity.setPrice(request.getPrice());
        }else{
            throw new ProductException(PRODUCT_PRICE_MINUS);
        }
        if(request.getSpec() != null && !request.getSpec().trim().equals(""))
            productEntity.setSpec(request.getSpec());

        return ProductEntity.toDto(productRepository.save(productEntity));
    }

    //가장 BOM의 구성에서 종속적인 경우 삭제 불가능함.
    @Override
    public void deleteProduct(Long id) {
        validateDeleteProduct(id);
        productRepository.deleteById(id);
    }

    //============================ Validates ===================================
    private void validateAddProduct(ProductDto.ProductAddRequest request){

        if(request.getName() == null || request.getName().trim().equals("")){
            throw new ProductException(PRODUCT_NAME_NULL);
        }

        if(request.getPrice() < 0){
            throw new ProductException(PRODUCT_PRICE_MINUS);
        }

    }

    private void validateDeleteProduct(Long id){

        boolean exists = productRepository.existsById(id);
        if(!exists){
            throw new ProductException(PRODUCT_NOT_FOUND);
        }

        if(bomRepository.existsByPidOrCid(id, id)){
            throw new BomException(BomErrorType.BOM_HAS_PRODUCT);
        }
    }

}
