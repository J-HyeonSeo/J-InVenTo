package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.type.CacheType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.jhsfully.inventoryManagement.type.ProductErrorType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @BeforeAll
    public static void setup(@Autowired DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/clean.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/product.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/bom.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private CacheManager redisCacheManager;

    private Cache redisCache; // 특정 Cache를 지우기 위한 변수

    @BeforeEach
    public void clearCache() {
        redisCacheManager.getCache(CacheType.ALL_PRODUCTS).clear();
        redisCacheManager.getCache(CacheType.ENABLE_PRODUCTS).clear();
    }

    @Autowired
    private ProductService productService;

    //======= Success Tests =======
    @Test
    @DisplayName("[Service]품목 가져오기 테스트 - Success")
    void getProductTestSuccess(){
        //when
        ProductDto.ProductResponse response = productService.getProduct(1L);

        //then
        assertEquals(1L, response.getId());
    }

    @Test
    @Order(1)
    @DisplayName("[Service]전품목(All types) 가져오기 테스트 - Success")
    void getAllProductsTestSuccess(){
        //when
        List<ProductDto.ProductResponse> products = productService.getAllProducts();
        //then
        assertEquals(10, products.size());
    }

    @Test
    @Order(2)
    @DisplayName("[Service]전품목(Only True) 가져오기 테스트 - Success")
    void getAllProductsOnlyTrueTestSuccess(){
        //when
        List<ProductDto.ProductResponse> products = productService.getProducts();
        //then
        assertEquals(9, products.size());
    }

    @Test
    @DisplayName("[Service]품목 추가 테스트 - Success")
    @Transactional
    void addProductTestSuccess(){

        //given
        ProductDto.ProductAddRequest request = ProductDto.ProductAddRequest.builder()
                .name("테스트품목")
                .company("테스트회사")
                .price(1000D)
                .spec("테스트규격")
                .build();

        //when
        ProductDto.ProductResponse response = productService.addProduct(request);

        //then
        ProductDto.ProductResponse productResponse = productService.getProduct(response.getId());

        assertAll(
                () -> assertEquals(response.getId(), productResponse.getId()),
                () -> assertEquals(response.getName(), productResponse.getName()),
                () -> assertEquals(response.getCompany(), productResponse.getCompany()),
                () -> assertEquals(response.getPrice(), productResponse.getPrice()),
                () -> assertEquals(response.getSpec(), productResponse.getSpec())
        );

    }

    @Test
    @DisplayName("[Service]품목 수정 테스트 - Success")
    @Transactional
    void updateProductTestSuccess(){
        //given
        ProductDto.ProductUpdateRequest request = ProductDto.ProductUpdateRequest
                .builder()
                .id(1L)
                .name("테스트수정이름")
                .company("테스트수정회사")
                .price(100D)
                .spec("테스트수정규격")
                .build();
        //when
        productService.updateProduct(request);

        //then
        ProductDto.ProductResponse product = productService.getProduct(1L);

        assertAll(
                () -> assertEquals(1L, product.getId()),
                () -> assertEquals("테스트수정이름", product.getName()),
                () -> assertEquals("테스트수정회사", product.getCompany()),
                () -> assertEquals(100D, product.getPrice()),
                () -> assertEquals("테스트수정규격", product.getSpec())
        );
    }

    @Test
    @DisplayName("[Service]품목 비활성화 테스트 - Success")
    @Transactional
    void disableProductTestSuccess(){
        //when
        productService.disableProduct(1L);

        //then
        ProductDto.ProductResponse product = productService.getProduct(1L);
        assertEquals(false, product.isEnabled());
    }
    @Test
    @DisplayName("[Service]품목 삭제 테스트 - Success")
    @Transactional
    void deleteProductTestSuccess(){
        //when
        productService.deleteProduct(1L);

        //then
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.getProduct(1L));
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }


    //======= Fail Tests =======

    @Test
    @DisplayName("[Service]품목 가져오기 테스트(품목X) - Fail")
    void getProductTestProductNotFoundFail(){

        //when
        ProductException exception = assertThrows(ProductException.class,
            () -> productService.getProduct(10000L));

        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());

    }

    @Test
    @DisplayName("[Service]품목 추가 테스트(이름 누락) - Fail")
    void addProductTestNameNullFail(){

        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.addProduct(ProductDto.ProductAddRequest
                                .builder()
                                .name(null)
                                .company("컴퍼니")
                                .price(1D)
                                .spec("스펙")
                                .build()));
        //then
        assertEquals(PRODUCT_NAME_NULL, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 추가 테스트(단가 누락) - Fail")
    void addProductTestPriceNullFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.addProduct(ProductDto.ProductAddRequest
                        .builder()
                        .name("이름")
                        .company("컴퍼니")
                        .price(null)
                        .spec("스펙")
                        .build()));
        //then
        assertEquals(PRODUCT_PRICE_NULL, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 추가 테스트(음수 단가) - Fail")
    void addProductTestPriceMinusFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.addProduct(ProductDto.ProductAddRequest
                        .builder()
                        .name("이름")
                        .company("컴퍼니")
                        .price(-1D)
                        .spec("스펙")
                        .build()));
        //then
        assertEquals(PRODUCT_PRICE_MINUS, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 수정 테스트(품목 X) - Fail")
    void updateProductTestProductNotFoundFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.updateProduct(ProductDto.ProductUpdateRequest
                        .builder()
                        .id(10000L)
                        .name("이름")
                        .company("컴퍼니")
                        .price(1D)
                        .spec("스펙")
                        .build()));
        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 수정 테스트(음수 단가) - Fail")
    void updateProductTestPriceMinusFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.updateProduct(ProductDto.ProductUpdateRequest
                        .builder()
                        .id(1L)
                        .name("이름")
                        .company("컴퍼니")
                        .price(-1D)
                        .spec("스펙")
                        .build()));
        //then
        assertEquals(PRODUCT_PRICE_MINUS, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 비활성화 테스트(품목 없음) - Fail")
    void disableProductTestProductNotFoundFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.deleteProduct(10000L));

        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 비활성화 테스트(BOM 종속) - Fail")
    void disableProductTestProductHasBomFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.deleteProduct(7L));

        //then
        assertEquals(PRODUCT_HAS_BOM, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 삭제 테스트(품목 없음) - Fail")
    void deleteProductTestProductNotFoundFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.deleteProduct(10000L));

        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]품목 삭제 테스트(BOM 존재) - Fail")
    void deleteProductTestBomHasProductFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> productService.deleteProduct(7L));

        //then
        assertEquals(PRODUCT_HAS_BOM, exception.getProductErrorType());
    }

    @Test //일단 구현 보류
    @Disabled
    @DisplayName("[Service]품목 삭제 테스트(테이블 연관) - Fail")
    void deleteProductTestIsReferencedFail(){

    }
}