package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
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
    void getPurchasesSuccess(){
        //when
        List<PurchaseDto.PurchaseResponse> purchases = purchaseService.getPurchases(
                LocalDate.of(2023, 6, 10),
                LocalDate.of(2023, 6, 11)
        );
        //then
        assertEquals(3, purchases.size());
    }

    //================ Fail Cases =======================

}