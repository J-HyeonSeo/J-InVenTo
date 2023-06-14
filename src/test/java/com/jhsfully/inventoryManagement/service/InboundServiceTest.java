package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.entity.InboundEntity;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.exception.PurchaseException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.repository.InboundRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.jhsfully.inventoryManagement.type.InboundErrorType.INBOUND_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.PurchaseErrorType.PURCHASE_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_NOT_FOUND;
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
        //when
        int count = inboundService.getInbounds(
                LocalDate.of(2023,6,13),
                LocalDate.of(2023,6,14)
        ).size();
        //then
        assertEquals(4, count);
    }

    @Nested
    @DisplayName("[Service] 입고 추가 -> 입고 삭제")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class InboundAdd_Delete{
        @Test
        @Order(1)
        @DisplayName("[Service] 입고 추가 - 성공")
        void addInboundSuccess(){
            //given
            InboundDto.InboundAddRequest request = InboundDto.InboundAddRequest
                    .builder()
                    .purchaseId(1L)
                    .stockId(1L) //임시로 하나 땡겨줌(원래 목적은 단순히 추가가 가능한지만 보는용도)
                    .amount(50D)
                    .note("a")
                    .build();

            //when
            Long inboundID = inboundService.addInbound(request).getId();
            //then
            InboundEntity inbound = inboundRepository.findById(inboundID).orElseThrow();

            assertAll(
                    () -> assertEquals(request.getPurchaseId(), inbound.getPurchase().getId()),
                    () -> assertEquals(request.getStockId(), inbound.getStock().getId()),
                    () -> assertEquals(request.getAmount(), inbound.getAmount()),
                    () -> assertEquals(request.getNote(), inbound.getNote())
            );
        }

        @Test
        @Order(2)
        @DisplayName("[Service] 입고 삭제 - 성공")
        void deleteInboundSuccess(){
            //when
            inboundService.deleteInbound(7L);
            //then
            InboundException exception = assertThrows(InboundException.class,
                    () -> inboundService.deleteInbound(7L));
            assertEquals(INBOUND_NOT_FOUND, exception.getInboundErrorType());
        }
    }

    //================== Fail Cases =============================

    //getInboundByPurchase()
    @Test
    @DisplayName("[Service] 입고 조회 실패 - (구매 X)")
    void getInboundByPurchaseFail(){
        //when
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> inboundService.getInboundsByPurchase(10000L));
        //then
        assertEquals(PURCHASE_NOT_FOUND, exception.getPurchaseErrorType());
    }

    //getInbound()
    @Test
    @DisplayName("[Service] 입고 조회 실패 - (입고 X)")
    void getInboundNotFoundFail(){
        //when
        InboundException exception = assertThrows(InboundException.class,
                () -> inboundService.getInbound(10000L));
        //then
        assertEquals(INBOUND_NOT_FOUND, exception.getInboundErrorType());
    }

    //addInbound()
    @Test
    @DisplayName("[Service] 입고 추가 실패 - (구매 X)")
    void addInboundPurchaseNotFoundFail(){
        //given
        InboundDto.InboundAddRequest request = InboundDto.InboundAddRequest
                .builder()
                .purchaseId(10000L)
                .stockId(1L) //임시로 하나 땡겨줌(원래 목적은 단순히 추가가 가능한지만 보는용도)
                .amount(50D)
                .note("a")
                .build();

        //when
        PurchaseException exception = assertThrows(PurchaseException.class,
                () -> inboundService.addInbound(request));
        //then
        assertEquals(PURCHASE_NOT_FOUND, exception.getPurchaseErrorType());
    }
    @Test
    @DisplayName("[Service] 입고 추가 실패 - (재고 X)")
    void addInboundStocksNotFoundFail(){
        //given
        InboundDto.InboundAddRequest request = InboundDto.InboundAddRequest
                .builder()
                .purchaseId(1L)
                .stockId(10000L) //임시로 하나 땡겨줌(원래 목적은 단순히 추가가 가능한지만 보는용도)
                .amount(50D)
                .note("a")
                .build();

        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> inboundService.addInbound(request));
        //then
        assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
    }

    //deleteInbound()
    @Test
    @DisplayName("[Service] 입고 삭제 실패 - (입고 X)")
    void deleteInboundNotFoundFail(){
        //when
        InboundException exception = assertThrows(InboundException.class,
                () -> inboundService.deleteInbound(10000L));
        //then
        assertEquals(INBOUND_NOT_FOUND, exception.getInboundErrorType());
    }
}