package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.entity.PurchaseEntity;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.exception.PurchaseException;
import com.jhsfully.inventoryManagement.repository.PurchaseRepository;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import com.jhsfully.inventoryManagement.type.PurchaseErrorType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.jhsfully.inventoryManagement.type.ProductErrorType.*;
import static com.jhsfully.inventoryManagement.type.PurchaseErrorType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PurchaseServiceTest {

    @BeforeAll
    static void setup(@Autowired DataSource dataSource){
        try(Connection conn = dataSource.getConnection()){
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/product.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/bom.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/purchase.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    PurchaseRepository purchaseRepository;

    //================ Success Cases ====================
    @Test
    @DisplayName("[Service]구매 가져오기 성공")
    @Order(1)
    void getPurchaseSuccess(){
        //when
        PurchaseDto.PurchaseResponse purchase = purchaseService.getPurchase(1L);
        //then
        assertAll(
                () -> assertEquals(1L, purchase.getProductId()),
                () -> assertEquals("전컴퍼니", purchase.getCompany()),
                () -> assertEquals(70000, purchase.getPrice()),
                () -> assertEquals(100, purchase.getAmount()),
                () -> assertEquals(LocalDateTime.of(2023, 6, 10, 16, 14, 14), purchase.getAt()),
                () -> assertEquals("전사장님으로부터 100개 어음발행.", purchase.getNote())
        );
    }

    @Test
    @DisplayName("[Service]구매 목록 가져오기 성공")
    @Order(2)
    void getPurchasesSuccess(){
        //when
        List<PurchaseDto.PurchaseResponse> purchases = purchaseService.getPurchases(
                LocalDate.of(2023, 6, 10),
                LocalDate.of(2023, 6, 11)
        );
        //then
        assertEquals(3, purchases.size());
    }

    @Test
    @DisplayName("[Service]구매 추가 성공")
    void addPurchaseSuccess(){
        //given
        PurchaseDto.PurchaseAddRequest request = PurchaseDto.PurchaseAddRequest
                .builder()
                .productId(10L)
                .company("미쳤다전자")
                .price(1000D)
                .amount(10D)
                .note("커패시터 10개 구매")
                .build();
        //when
        Long purchaseID = purchaseService.addPurchase(request).getId();
        //then
        PurchaseEntity purchase = purchaseRepository.findById(
                purchaseID).orElseThrow();

        assertAll(
                () -> assertEquals(request.getProductId(), purchase.getProduct().getId()),
                () -> assertEquals(request.getCompany(), purchase.getCompany()),
                () -> assertEquals(request.getPrice(), purchase.getPrice()),
                () -> assertEquals(request.getAmount(), purchase.getAmount()),
                () -> assertEquals(request.getNote(), purchase.getNote())
        );
    }

    @Test
    @DisplayName("[Service]구매 삭제 성공")
    @Transactional
    void deletePurchaseSuccess(){
        //when
        purchaseService.deletePurchase(1L);
        //then
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> purchaseService.deletePurchase(1L));
        assertEquals(PURCHASE_NOT_FOUND, exception.getPurchaseErrorType());
    }

    //================ Fail Cases =======================

    //getPurchase()
    @Test
    @DisplayName("[Service]구매 조회 실패 - (구매 X)")
    void getPurchaseNotFoundFail(){
        //when
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> purchaseService.getPurchase(10000L));
        //then
        assertEquals(PURCHASE_NOT_FOUND, exception.getPurchaseErrorType());
    }

    //addPurchase()
    @Test
    @DisplayName("[Service]구매 추가 실패 - (Amount X)")
    void addPurchaseAmountNullFail(){
        //given
        PurchaseDto.PurchaseAddRequest request = PurchaseDto.PurchaseAddRequest
                .builder()
                .productId(10L)
                .company("미쳤다전자")
                .price(1000D)
                .amount(null)
                .note("커패시터 10개 구매")
                .build();
        //when
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> purchaseService.addPurchase(request));

        //then
        assertEquals(PURCHASE_AMOUNT_NULL, exception.getPurchaseErrorType());
    }

    @Test
    @DisplayName("[Service]구매 추가 실패 - (Amount <= 0)")
    void addPurchaseAmountOrLessZeroFail(){
        //given
        PurchaseDto.PurchaseAddRequest request = PurchaseDto.PurchaseAddRequest
                .builder()
                .productId(10L)
                .company("미쳤다전자")
                .price(1000D)
                .amount(0D)
                .note("커패시터 10개 구매")
                .build();
        //when
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> purchaseService.addPurchase(request));

        //then
        assertEquals(PURCHASE_AMOUNT_LESS_ZERO, exception.getPurchaseErrorType());
    }

    @Test
    @DisplayName("[Service]구매 추가 실패 - (price X)")
    void addPurchasePriceNullFail(){
        //given
        PurchaseDto.PurchaseAddRequest request = PurchaseDto.PurchaseAddRequest
                .builder()
                .productId(10L)
                .company("미쳤다전자")
                .price(null)
                .amount(1000D)
                .note("커패시터 10개 구매")
                .build();
        //when
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> purchaseService.addPurchase(request));

        //then
        assertEquals(PURCHASE_PRICE_NULL, exception.getPurchaseErrorType());
    }

    @Test
    @DisplayName("[Service]구매 추가 실패 - (price <= 0)")
    void addPurchasePriceOrLessZeroFail(){
        //given
        PurchaseDto.PurchaseAddRequest request = PurchaseDto.PurchaseAddRequest
                .builder()
                .productId(10L)
                .company("미쳤다전자")
                .price(0D)
                .amount(1000D)
                .note("커패시터 10개 구매")
                .build();
        //when
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> purchaseService.addPurchase(request));

        //then
        assertEquals(PURCHASE_PRICE_LESS_ZERO, exception.getPurchaseErrorType());
    }

    @Test
    @DisplayName("[Service]구매 추가 실패 - (product x)")
    void addPurchaseProductNotFoundFail(){
        //given
        PurchaseDto.PurchaseAddRequest request = PurchaseDto.PurchaseAddRequest
                .builder()
                .productId(10000L)
                .company("미쳤다전자")
                .price(1000D)
                .amount(1000D)
                .note("커패시터 10개 구매")
                .build();
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> purchaseService.addPurchase(request));

        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]구매 추가 실패 - (product is bom parent)")
    void addPurchaseCantParentProductFail(){
        //given
        PurchaseDto.PurchaseAddRequest request = PurchaseDto.PurchaseAddRequest
                .builder()
                .productId(6L)
                .company("미쳤다전자")
                .price(1000D)
                .amount(1000D)
                .note("커패시터 10개 구매")
                .build();
        //when
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> purchaseService.addPurchase(request));

        //then
        assertEquals(PURCHASE_CANT_PARENT_PRODUCT, exception.getPurchaseErrorType());
    }

    //deletePurchase()
    @Test
    @DisplayName("[Service]구매 삭제 실패 - (구매 X)")
    void deletePurchaseNotFoundFail(){
        //when
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> purchaseService.deletePurchase(10000L));
        //then
        assertEquals(PURCHASE_NOT_FOUND, exception.getPurchaseErrorType());
    }
    @Test
    @Disabled
    @DisplayName("[Service]구매 삭제 실패 - (입고 잡혀있음 X)")
    void deletePurchaseHasInboundFail(){
        //given
        //when
        //then
    }
}