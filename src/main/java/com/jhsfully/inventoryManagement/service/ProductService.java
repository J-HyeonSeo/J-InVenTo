package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.entity.ProductEntity;
import com.jhsfully.inventoryManagement.repository.BomRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.CacheType.*;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.*;

@Service
@Transactional
@AllArgsConstructor
public class ProductService implements ProductInterface{

    private final ProductRepository productRepository;
    private final BomRepository bomRepository;

    @Override
    @Transactional(readOnly = true)
    public ProductDto.ProductResponse getProduct(Long id){
        return ProductEntity.toDto(productRepository.findById(id)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND)));
    }

    //Enabled 여부에 상관없이 모두 가져옴.(클라이언트 단에서 기본적으로 활성화된 품목만 보여줌)
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = ALL_PRODUCTS, cacheManager = "redisCacheManager")
    public List<ProductDto.ProductResponse> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(ProductEntity::toDto)
                .collect(Collectors.toList());
    }

    //Enabled == True 인 프로덕트만 가져옴.
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = ENABLE_PRODUCTS, cacheManager = "redisCacheManager")
    public List<ProductDto.ProductResponse> getProducts(){
        List<ProductEntity> productEntities = productRepository.findByEnabledIsTrue();
        return productEntities.stream()
                .map(ProductEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(cacheNames = {ALL_PRODUCTS, ENABLE_PRODUCTS}, cacheManager = "redisCacheManager", allEntries = true)
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
    @CacheEvict(cacheNames = {ALL_PRODUCTS, ENABLE_PRODUCTS, BOM_TOP, BOM_TREE, BOM_LEAF},
            cacheManager = "redisCacheManager", allEntries = true)
    public ProductDto.ProductResponse updateProduct(ProductDto.ProductUpdateRequest request) {
        ProductEntity productEntity = productRepository.findById(request.getId())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if(request.getName() != null && !request.getName().trim().equals(""))
            productEntity.setName(request.getName());
        if(request.getCompany() != null && !request.getCompany().trim().equals(""))
            productEntity.setCompany(request.getCompany());

        if(request.getPrice() != null) {
            if (request.getPrice() >= 0) {
                productEntity.setPrice(request.getPrice());
            } else {
                throw new ProductException(PRODUCT_PRICE_MINUS);
            }
        }

        if(request.getSpec() != null && !request.getSpec().trim().equals(""))
            productEntity.setSpec(request.getSpec());

        if(request.getEnabled() != null){
            productEntity.setEnabled(request.getEnabled());
        }

        return ProductEntity.toDto(productRepository.save(productEntity));
    }

    //가장 BOM의 구성에서 종속적인 경우 비활성화도 불가능함.
    @Override
    @CacheEvict(cacheNames = {ALL_PRODUCTS, ENABLE_PRODUCTS}, cacheManager = "redisCacheManager", allEntries = true)
    public void disableProduct(Long id) {
        ProductEntity product = validateDisableProduct(id);
        product.setEnabled(false); //비활성화만 시킴(단순히 목록에서 보여지지 않음)
        productRepository.save(product);
    }

    //연관 데이터에 하나도 포함되지 않은 경우만 삭제가능함.
    @Override
    @CacheEvict(cacheNames = {ALL_PRODUCTS, ENABLE_PRODUCTS}, cacheManager = "redisCacheManager", allEntries = true)
    public void deleteProduct(Long id) {
        ProductEntity product = validateDisableProduct(id);
        validateDeleteProduct(product);

        productRepository.delete(product);
    }

    //============================ Validates ===================================
    private void validateAddProduct(ProductDto.ProductAddRequest request){

        if(request.getName() == null || request.getName().trim().equals("")){
            throw new ProductException(PRODUCT_NAME_NULL);
        }

        if(request.getPrice() == null){
            throw new ProductException(PRODUCT_PRICE_NULL);
        }

        if(request.getPrice() < 0){ //나중에 <= 0 으로 수정하고 에러메세지 바꿔야 할 것!!
            throw new ProductException(PRODUCT_PRICE_MINUS);
        }

    }

    private ProductEntity validateDisableProduct(Long id){

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if(bomRepository.existsByParentProductOrChildProduct(product, product)){
            throw new ProductException(PRODUCT_HAS_BOM);
        }
        return product;
    }

    private void validateDeleteProduct(ProductEntity product){

        if (productRepository.isProductReferenced(product)) {
            throw new ProductException(PRODUCT_IS_REFERENCED);
        }

    }

}
