package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.repository.InboundRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InboundServiceTest {

    @BeforeAll
    static void setup(@Autowired DataSource dataSource){
        try(Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/clean.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/product.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/bom.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/purchase.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/stocks.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/inbound.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    InboundService inboundService;

    @Autowired
    InboundRepository inboundRepository;

    //================== Success Cases ==========================

    @Test
    @DisplayName("[Service] 입고를 구매로 가져오기 - 성공")
    void getInboundByPurchaseSuccess(){
        //when
        Double count = inboundService.getInboundsByPurchase(1L);
        //then
        assertEquals(20D, count);
    }

    @Test
    @DisplayName("[Service] 입고 가져오기 - 성공")
    void getInboundSuccess(){
        //when
        InboundDto.InboundResponse inbound = inboundService.getInbound(1L);
        //then
        assertAll(
                () -> assertEquals(1L, inbound.getPurchaseId()),
                () -> assertEquals(1L, inbound.getStockId()),
                () -> assertEquals(10D, inbound.getAmount()),
                () -> assertEquals(
                        LocalDateTime.of(2023, 6, 11,
                                18, 14, 14),
                        inbound.getInboundAt()),
                () -> assertEquals("타이어입고", inbound.getNote())
        );
    }

    @Test
    @DisplayName("[Service] 입고 범위 목록 가져오기 - 성공")
    void getInboundsSuccess(){
        //given
        //when
        //then
    }

    @Nested
    @DisplayName("[Service] 입고 추가 -> 입고 삭제")
    class InboundAdd_Delete{
        @Test
        @DisplayName("[Service] 입고 추가 - 성공")
        void addInboundSuccess(){
            //given


            //when
            //then
        }

        @Test
        @DisplayName("[Service] 입고 삭제 - 성공")
        void deleteInboundSuccess(){
            //given
            //when
            //then
        }
    }

    //================== Fail Cases =============================

    //getInboundByPurchase()
    @Test
    @DisplayName("[Service] 입고 조회 실패 - (구매 X)")
    void getInboundByPurchaseFail(){
        //given
        //when
        //then
    }

    //getInbound()
    @Test
    @DisplayName("[Service] 입고 조회 실패 - (입고 X)")
    void getInboundNotFoundFail(){
        //given
        //when
        //then
    }

    //addInbound()
    @Test
    @DisplayName("[Service] 입고 추가 실패 - (구매 X)")
    void addInboundPurchaseNotFoundFail(){
        //given
        //when
        //then
    }
    @Test
    @DisplayName("[Service] 입고 추가 실패 - (재고 X)")
    void addInboundStocksNotFoundFail(){
        //given
        //when
        //then
    }

    //deleteInbound()
    @Test
    @DisplayName("[Service] 입고 삭제 실패 - (입고 X)")
    void deleteInboundNotFoundFail(){
        //given
        //when
        //then
    }
}