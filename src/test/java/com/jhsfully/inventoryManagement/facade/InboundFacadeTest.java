package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.service.InboundInterface;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.jhsfully.inventoryManagement.type.InboundErrorType.INBOUND_EXCEED_PURCHASE_AMOUNT;
import static com.jhsfully.inventoryManagement.type.InboundErrorType.INBOUND_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_OCCURRED_OUTBOUND;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InboundFacadeTest {

    @BeforeAll
    static void setup(@Autowired DataSource dataSource){
        try(Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/clean.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/product.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/bom.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/purchase.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/stocks.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/inbound.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/outbound.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/outbounddetails.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    InboundFacade inboundFacade;

    @Autowired
    InboundInterface inboundService;

    @Autowired
    StocksInterface stocksService;

    //================ Success Cases ==================

    @Test
    @Order(1)
    @DisplayName("[Facade] 입고 수행 성공")
    void executeInboundSuccess(){
        //given
        InboundDto.InboundOuterAddRequest request = InboundDto.InboundOuterAddRequest
                .builder()
                .purchaseId(1L)
                .amount(80D)
                .note("타이어 입고수행")
                .build();
        //when
        Long inboundId = inboundFacade.executeInbound(request);

        //then
        InboundDto.InboundResponse inbound = inboundService.getInbound(inboundId);

        StocksDto.StockResponseLot stock = stocksService.getStock(inbound.getStockId());

        assertAll(
                () -> assertEquals(request.getPurchaseId(), inbound.getPurchaseId()),
                () -> assertEquals(request.getAmount(), inbound.getAmount()),
                () -> assertEquals(request.getAmount(), stock.getAmount()),
                () -> assertEquals(request.getNote(), inbound.getNote()),
                () -> assertEquals(inbound.getStockId(), stock.getId())
        );
    }

    @Test
    @Order(2)
    @DisplayName("[Facade] 입고 취소 성공")
    void cancelInboundSuccess(){
        //when
        inboundFacade.cancelInbound(7L);
        //then
        InboundException exceptionI = assertThrows(InboundException.class,
                () -> inboundService.getInbound(7L));
        StocksException exceptionS = assertThrows(StocksException.class,
                () -> stocksService.getStock(7L));
        assertAll(
                () -> assertEquals(INBOUND_NOT_FOUND, exceptionI.getInboundErrorType()),
                () -> assertEquals(STOCKS_NOT_FOUND, exceptionS.getStocksErrorType())
        );
    }

    //=============== Fail Cases =======================

    @Test
    @DisplayName("[Facade] 입고 수행 실패 - 수량 초과")
    void executeInboundFail(){
        //given
        InboundDto.InboundOuterAddRequest request = InboundDto.InboundOuterAddRequest
                .builder()
                .purchaseId(1L)
                .amount(81D)
                .note("타이어 입고수행")
                .build();
        //when
        InboundException exception = assertThrows(InboundException.class,
                () -> inboundFacade.executeInbound(request));

        //then
        assertEquals(INBOUND_EXCEED_PURCHASE_AMOUNT, exception.getInboundErrorType());
    }

    @Test
    @DisplayName("[Facade] 입고 취소 실패 - (출고 발생)")
    void cancelInboundFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> inboundFacade.cancelInbound(1L));
        //then
        assertEquals(STOCKS_OCCURRED_OUTBOUND, exception.getStocksErrorType());
    }
}