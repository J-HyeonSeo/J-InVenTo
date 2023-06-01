package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.exception.BomException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.repository.BomRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.type.BomErrorType;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    //Mocking and Inject
    @Mock
    private ProductRepository productRepository;

    @Mock
    private BomRepository bomRepository;

    @InjectMocks
    private ProductService productService;

    //======= Success Tests =======
    @Test
    @DisplayName("[Service]전품목 가져오기 테스트 - Success")
    void getProductsTestSuccess(){
        //given
        List<ProductEntity> responses =
                new ArrayList<>(Arrays.asList(
                        ProductEntity.builder()
                                .id(1L)
                                .name("TestName1")
                                .company("TestCompany1")
                                .price(1D)
                                .spec("TestSpec1")
                                .build()
                        ,ProductEntity.builder()
                                .id(2L)
                                .name("TestName2")
                                .company("TestCompany2")
                                .price(2D)
                                .spec("TestSpec2")
                                .build()
                ));
        given(productRepository.findAll())
                .willReturn(responses);
        //when

        List<ProductDto.ProductResponse> products = productService.getProducts();

        //then
        verify(productRepository, times(1)).findAll();

        assertEquals(2, products.size());
        assertEquals(1L, products.get(0).getId());
        assertEquals("TestName1", products.get(0).getName());
        assertEquals("TestCompany1", products.get(0).getCompany());
        assertEquals(1, products.get(0).getPrice());
        assertEquals("TestSpec1", products.get(0).getSpec());

        assertEquals(2L, products.get(1).getId());
        assertEquals("TestName2", products.get(1).getName());
        assertEquals("TestCompany2", products.get(1).getCompany());
        assertEquals(2, products.get(1).getPrice());
        assertEquals("TestSpec2", products.get(1).getSpec());
    }

    @Test
    @DisplayName("[Service]품목 추가 테스트 - Success")
    void addProductTestSuccess(){
        //given
        given(productRepository.save(any()))
                .willReturn(ProductEntity.builder()
                        .id(1L)
                        .name("TestName1")
                        .company("TestCompany1")
                        .price(1D)
                        .spec("TestSpec1")
                        .build());

        //캡터를 사용해야, 진짜 Repository에 저장시키려는 값을 확인할 수 있음.
        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);

        //when
        ProductDto.ProductResponse productResponse = productService.addProduct(ProductDto.ProductAddRequest.builder()
                .name("TestName1")
                .company("TestCompany1")
                .price(1D)
                .spec("TestSpec1")
                .build());

        //then

        verify(productRepository, times(1)).save(captor.capture());
        assertEquals(1L, productResponse.getId());
        assertEquals("TestName1", captor.getValue().getName());
        assertEquals("TestCompany1", captor.getValue().getCompany());
        assertEquals(1, captor.getValue().getPrice());
        assertEquals("TestSpec1", captor.getValue().getSpec());

    }

    @Test
    @DisplayName("[Service]품목 수정 테스트 - Success")
    void updateProductTestSuccess(){
        //given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(
                        ProductEntity.builder()
                                .id(10L)
                                .name("testName")
                                .company("testCompany")
                                .price(1000D)
                                .spec("testSpec")
                                .build()
                ));
        given(productRepository.save(any()))
                .willReturn(
                        ProductEntity.builder()
                                .id(10L)
                                .name("testName")
                                .company("testCompany")
                                .price(1000D)
                                .spec("testSpec")
                                .build()
                );
        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);

        //when
        productService.updateProduct(ProductDto.ProductUpdateRequest.builder()
                        .id(10L)
                        .name("newName")
                        .company("newCompany")
                        .price(2000D)
                        .spec("newSpec")
                        .build());

        //then
        verify(productRepository, times(1)).save(captor.capture());

        assertEquals(10L, captor.getValue().getId());
        assertEquals("newName", captor.getValue().getName());
        assertEquals("newCompany", captor.getValue().getCompany());
        assertEquals(2000, captor.getValue().getPrice());
        assertEquals("newSpec", captor.getValue().getSpec());
    }

    @Test
    @DisplayName("[Service]품목 삭제 테스트 - Success")
    void deleteProductTestSuccess(){
        //given
        given(productRepository.existsById(anyLong()))
                .willReturn(true);
        given(bomRepository.existsByPidOrCid(anyLong(), anyLong()))
                .willReturn(false);

        //when
        productService.deleteProduct(123L);

        //then
        verify(productRepository, times(1)).deleteById(123L);
    }


    //======= Fail Tests =======

    @Test
    @DisplayName("[Service]품목 추가 테스트(이름 누락) - Fail")
    void addProductTestNameNullFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.addProduct(
                        ProductDto.ProductAddRequest.builder()
                                .name(null)
                                .company("com")
                                .price(1D)
                                .spec("spec")
                                .build()
                ));

        //then
        assertEquals(ProductErrorType.PRODUCT_NAME_NULL, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 추가 테스트(단가 누락) - Fail")
    void addProductTestPriceNullFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.addProduct(
                        ProductDto.ProductAddRequest.builder()
                                .name("name")
                                .company("com")
                                .spec("spec")
                                .build()
                ));

        //then
        assertEquals(ProductErrorType.PRODUCT_PRICE_NULL, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 추가 테스트(음수 단가) - Fail")
    void addProductTestPriceMinusFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.addProduct(
                        ProductDto.ProductAddRequest.builder()
                                .name("name")
                                .company("com")
                                .price(-0.1)
                                .spec("spec")
                                .build()
                ));

        //then
        assertEquals(ProductErrorType.PRODUCT_PRICE_MINUS, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 수정 테스트(품목 X) - Fail")
    void updateProductTestProductNotFoundFail(){
        //given
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.updateProduct(
                        ProductDto.ProductUpdateRequest.builder()
                                .id(1L)
                                .build()
                ));

        //then
        assertEquals(ProductErrorType.PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 수정 테스트(음수 단가) - Fail")
    void updateProductTestPriceMinusFail(){
        //given
        given(productRepository.findById(any()))
                .willReturn(Optional.of(
                        ProductEntity.builder()
                                .id(10L)
                                .name("testName")
                                .company("testCompany")
                                .price(100D)
                                .spec("testSpec")
                                .build()
                ));
        //when
        ProductException exception = assertThrows(ProductException.class, () ->
                productService.updateProduct(ProductDto.ProductUpdateRequest.builder()
                        .id(10L)
                        .name("testName")
                        .company("testCompany")
                        .price(-0.1)
                        .spec("testSpec")
                        .build())
        );

        //then
        assertEquals(ProductErrorType.PRODUCT_PRICE_MINUS, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 삭제 테스트(품목 없음) - Fail")
    void deleteProductTestProductNotFoundFail(){
        //given
        given(productRepository.existsById(anyLong()))
                .willReturn(false);
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.deleteProduct(1L));
        //then
        assertEquals(ProductErrorType.PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 삭제 테스트(BOM 존재) - Fail")
    void deleteProductTestBomHasProductFail(){
        //given
        given(productRepository.existsById(anyLong()))
                .willReturn(true);
        given(bomRepository.existsByPidOrCid(anyLong(), anyLong()))
                .willReturn(true);
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.deleteProduct(1L));
        //then
        assertEquals(ProductErrorType.PRODUCT_HAS_BOM, exception.getProductErrorType());
    }
}