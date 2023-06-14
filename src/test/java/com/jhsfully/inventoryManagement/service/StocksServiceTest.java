package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.entity.StocksEntity;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.repository.StocksRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.jhsfully.inventoryManagement.type.InboundErrorType.INBOUND_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StocksServiceTest {

    @BeforeAll
    static void setup(@Autowired DataSource dataSource){
        try(Connection conn = dataSource.getConnection()) {
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
    StocksService stocksService;

    @Autowired
    StocksRepository stocksRepository;

    //============ Success Cases ===============
    @Test
    @DisplayName("[Service] 재고 가져오기 성공")
    void getStockSuccess(){
        //when
        StocksDto.StockResponseLot stock = stocksService.getStock(1L);
        //then
        assertAll(
                () -> assertEquals(1L, stock.getProductId()),
                () -> assertEquals(10D, stock.getAmount()),
                () -> assertEquals(LocalDate.of(2023,6,11),
                        stock.getLot())
        );
    }

    @Test
    @DisplayName("[Service] 전재고 가져오기 성공")
    void getStocksSuccess(){
        //when
        int count = stocksService.getAllStocks().size();
        //then
        assertEquals(5, count);
    }

    @Test
    @DisplayName("[Service] 로트 가져오기 성공")
    void getLotByProductSuccess(){
        //when
        int count = stocksService.getLotByPid(1L).size();
        //then
        assertEquals(2, count);
    }


    @Nested
    @DisplayName("[Service] 재고 추가 -> 입고 할당 -> 입고 해제 -> 입고 삭제")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class addStockAndSetInboundAndReleaseInbound{

        @Test
        @Order(1)
        @DisplayName("[Service] 재고 추가 성공")
        void addStockSuccess(){
            //given
            StocksDto.StockAddRequest request = StocksDto.StockAddRequest
                    .builder()
                    .productId(1L)
                    .company("전컴퍼니")
                    .amount(10D)
                    .lot(LocalDate.of(2023, 6, 1))
                    .build();
            //when
            Long stockID = stocksService.addStock(request).getId();

            //then
            StocksEntity stock = stocksRepository.findById(stockID).orElseThrow();

            assertAll(
                    () -> assertEquals(request.getProductId(), stock.getProduct().getId()),
                    () -> assertEquals(request.getAmount(), stock.getAmount()),
                    () -> assertEquals(request.getLot(), stock.getLot())
            );

        }

        @Test
        @Order(2)
        @DisplayName("[Service] 입고 할당 성공")
        void setInboundSuccess(){
            //when
            stocksService.setInbound(7L, 1L);
            //then
            StocksEntity stock = stocksRepository.findById(7L).orElseThrow();
            assertEquals(1L, stock.getInbound().getId());
        }

        @Test
        @Order(3)
        @DisplayName("[Service] 입고 해제 성공")
        void releaseInboundSuccess(){
            //when
            stocksService.releaseInbound(7L);
            //then
            StocksEntity stock = stocksRepository.findById(7L).orElseThrow();
            assertEquals(null, stock.getInbound());
        }

        @Test
        @Order(3)
        @DisplayName("[Service] 재고 삭제 성공")
        void deleteStockSuccess(){
            //when
            stocksService.deleteStock(7L);
            //then
            StocksException exception = assertThrows(StocksException.class,
                    () -> stocksService.deleteStock(7L));
            assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
        }
    }

    @Test
    @DisplayName("[Service] 재고 차감 성공")
    void spendStockSuccess(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service] 재고 차감 취소 성공")
    void cancelSpendStockSuccess(){
        //given
        //when
        //then
    }

    //================== Fail Cases ====================

    //getStock()
    @Test
    @DisplayName("[Service] 재고 조회 실패 - (재고 X)")
    void getStockNotFoundFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.getStock(10000L));
        //then
        assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
    }

    //getLotById()
    @Test
    @DisplayName("[Service] 재고 로트 조회 실패 - (품목 X)")
    void getLotProductNotFoundFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> stocksService.getLotByPid(1000L));
        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    //addStock()
    @Test
    @DisplayName("[Service] 재고 추가 실패 - (수량 X)")
    void addStockAmountNullFail(){
        //given
        StocksDto.StockAddRequest request = StocksDto.StockAddRequest
                .builder()
                .productId(1L)
                .company("전컴퍼니")
                .amount(null)
                .lot(LocalDate.of(2023, 6, 1))
                .build();
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.addStock(request));

        //then
        assertEquals(STOCKS_AMOUNT_NULL, exception.getStocksErrorType());
    }

    @Test
    @DisplayName("[Service] 재고 추가 실패 - (수량 <= 0)")
    void addStockAmountOrLessZeroFail(){
        //given
        StocksDto.StockAddRequest request = StocksDto.StockAddRequest
                .builder()
                .productId(1L)
                .company("전컴퍼니")
                .amount(0D)
                .lot(LocalDate.of(2023, 6, 1))
                .build();
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.addStock(request));

        //then
        assertEquals(STOCKS_NOT_CREATE_OR_LESS_ZERO, exception.getStocksErrorType());
    }

    @Test
    @DisplayName("[Service] 재고 추가 실패 - (품목 X)")
    void addStockProductNotFoundFail(){
        //given
        StocksDto.StockAddRequest request = StocksDto.StockAddRequest
                .builder()
                .productId(10000L)
                .company("전컴퍼니")
                .amount(1D)
                .lot(LocalDate.of(2023, 6, 1))
                .build();
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> stocksService.addStock(request));

        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }


    //setInbound()

    @Test
    @DisplayName("[Service] 입고 할당 실패 - (재고 X)")
    void setInboundStockNotFoundFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.setInbound(10000L, 1L));

        //then
        assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
    }

    @Test
    @DisplayName("[Service] 입고 할당 실패 - (입고 X)")
    void setInboundInboundNotFoundFail(){
        //when
        InboundException exception = assertThrows(InboundException.class,
                () -> stocksService.setInbound(1L, 10000L));

        //then
        assertEquals(INBOUND_NOT_FOUND, exception.getInboundErrorType());
    }

    //releaseInbound()
    @Test
    @DisplayName("[Service] 입고 해제 실패 - (재고 X)")
    void releaseInboundStockNotFoundFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.releaseInbound(10000L));

        //then
        assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
    }

    //spendStockById()
    @Test
    @DisplayName("[Service] 재고 차감 실패 - (수량 X)")
    void spendStockAmountNullFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.spendStockById(1L, null));

        //then
        assertEquals(STOCKS_AMOUNT_NULL, exception.getStocksErrorType());
    }
    @Test
    @DisplayName("[Service] 재고 차감 실패 - (수량 <= 0)")
    void spendStockAmountOrLessZeroFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.spendStockById(1L, -1D));

        //then
        assertEquals(STOCKS_CANT_SPEND_OR_LESS_ZERO, exception.getStocksErrorType());
    }
    @Test
    @DisplayName("[Service] 재고 차감 실패 - (requestAmount > nowAmount)")
    void spendStockAmountExceedFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.spendStockById(1L, 11D));

        //then
        assertEquals(STOCKS_SPEND_AMOUNT_EXCEED, exception.getStocksErrorType());
    }
    @Test
    @DisplayName("[Service] 재고 차감 실패 - (재고 X)")
    void spendStockNotFoundFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.spendStockById(10000L, 10D));

        //then
        assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
    }

    //cancelSpendStockById()
    @Test
    @DisplayName("[Service] 재고 차감 취소 실패 - (수량 X)")
    void cancelSpendStockAmountNullFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.cancelSpendStockById(1L, null));

        //then
        assertEquals(STOCKS_AMOUNT_NULL, exception.getStocksErrorType());
    }
    @Test
    @DisplayName("[Service] 재고 차감 취소 실패 - (수량 <= 0)")
    void cancelSpendCantCancelSpendFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.cancelSpendStockById(1L, 0D));

        //then
        assertEquals(STOCKS_CANT_CANCEL_OR_LESS_ZERO, exception.getStocksErrorType());
    }
    @Test
    @DisplayName("[Service] 재고 차감 취소 실패 - (재고 X)")
    void cancelSpendStockNotFoundFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.cancelSpendStockById(10000L, 100D));

        //then
        assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
    }

    //deleteStock()
    @Test
    @DisplayName("[Service] 재고 삭제 실패 - (재고 X)")
    void deleteStocksNotFoundFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> stocksService.deleteStock(10000L));

        //then
        assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
    }
}