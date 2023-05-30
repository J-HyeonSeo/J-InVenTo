package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    //Mocking and Inject
    @Mock
    private ProductRepository productRepository;

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
                                .price(1)
                                .spec("TestSpec1")
                                .build()
                        ,ProductEntity.builder()
                                .id(2L)
                                .name("TestName2")
                                .company("TestCompany2")
                                .price(2)
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
    @DisplayName("[Service]품목 추가 테스트 - S")
    void addProductTestSuccess(){
        //given
        given(productRepository.save(any()))
                .willReturn(ProductEntity.builder()
                        .id(1L)
                        .name("TestName1")
                        .company("TestCompany1")
                        .price(1)
                        .spec("TestSpec1")
                        .build());

        //캡터를 사용해야, 진짜 Repository에 저장시키려는 값을 확인할 수 있음.
        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);

        //when
        ProductDto.ProductResponse productResponse = productService.addProduct(ProductDto.ProductAddRequest.builder()
                .name("TestName1")
                .company("TestCompany1")
                .price(1)
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

    //======= Fail Tests =======

}