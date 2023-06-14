package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.entity.OutboundDetailsEntity;
import com.jhsfully.inventoryManagement.entity.OutboundEntity;
import com.jhsfully.inventoryManagement.exception.OutboundException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.repository.OutboundDetailRepository;
import com.jhsfully.inventoryManagement.repository.OutboundRepository;
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
import java.util.Arrays;
import java.util.List;

import static com.jhsfully.inventoryManagement.type.OutboundErrorType.*;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OutboundServiceTest {

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
    OutboundService outboundService;

    @Autowired
    OutboundRepository outboundRepository;

    @Autowired
    OutboundDetailRepository outboundDetailRepository;


    //================= Success Cases ====================

    @Test
    @DisplayName("[Service]재고에 대한 출고 개수 조회 - 성공")
    void countByStockSuccess(){
        //when
        Double sum = outboundService.countByStock(1L);
        //then
        assertEquals(2, sum);
    }

    @Test
    @DisplayName("[Service]출고 이력 조회 성공")
    void getOutboundsSuccess(){
        //when
        List<OutboundDto.OutboundResponse> outbounds =
                outboundService.getOutbounds(LocalDate.of(2023, 6, 12),
                LocalDate.of(2023, 6, 12));
        //then

        assertAll(
                () -> assertEquals(1, outbounds.size()),
                () -> assertEquals(1, outbounds.get(0).getId()),
                () -> assertEquals(5, outbounds.get(0).getAmount()),
                () -> assertEquals("타이어", outbounds.get(0).getProductName()),
                () -> assertEquals(
                        LocalDateTime.of(2023,6,12,18,14,14),
                        outbounds.get(0).getAt()),
                () -> assertEquals("a", outbounds.get(0).getNote())
        );

    }

    @Test
    @DisplayName("[Service]출고 이력 상세 조회 성공")
    void getOutboundDetailsSuccess(){
        //when
        List<OutboundDto.OutboundDetailResponse> outboundDetails =
                outboundService.getOutboundDetails(1L);
        //then
        assertAll(
                () -> assertEquals(1, outboundDetails.size()),
                () -> assertEquals(1, outboundDetails.get(0).getStockId()),
                () -> assertEquals(5, outboundDetails.get(0).getAmount())
        );

    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("[Service] 출고 -> 삭제 프로세스 성공")
    class Add_Delete_Process {
        @Test
        @Order(1)
        @DisplayName("[Service]출고 추가 성공")
        void addOutboundSuccess() {
            //given
            OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                    .builder()
                    .productId(1L)
                    .stocks(Arrays.asList(new OutboundDto.stock(1L, 5D)))
                    .amount(5D)
                    .destination("ccc")
                    .note("ddd")
                    .build();
            //when
            OutboundDto.OutboundResponse response = outboundService.addOutbound(request);
            //then
            OutboundEntity outbound = outboundRepository.findById(response.getId()).orElseThrow();

            assertAll(
                    () -> assertEquals(request.getProductId(), outbound.getProduct().getId()),
                    () -> assertEquals(request.getDestination(), outbound.getDestination()),
                    () -> assertEquals(request.getAmount(), outbound.getAmount()),
                    () -> assertEquals(request.getNote(), outbound.getNote())
            );
        }

        @Test
        @Order(2)
        @DisplayName("[Service]출고 상세 추가 성공")
        void addOutboundDetailSuccess() {
            //given
            OutboundDto.OutboundDetailAddRequest request =
                    OutboundDto.OutboundDetailAddRequest.builder()
                            .outboundId(3L)
                            .stockId(1L)
                            .amount(5D)
                            .build();
            //when
            outboundService.addOutboundDetail(request);
            //then
            OutboundDetailsEntity outboundDetail =
                    outboundDetailRepository.findById(3L).orElseThrow();

            assertAll(
                    () -> assertEquals(request.getOutboundId(), outboundDetail.getOutbound().getId()),
                    () -> assertEquals(request.getStockId(), outboundDetail.getStock().getId()),
                    () -> assertEquals(request.getAmount(), outboundDetail.getAmount())
            );
        }

        @Test
        @Order(4)
        @DisplayName("[Service]출고 삭제 성공")
        void deleteOutboundSuccess() {
            //when
            outboundService.deleteOutbound(3L);
            //then
            OutboundException exception = assertThrows(OutboundException.class,
                    () -> outboundService.deleteOutbound(3L));
            assertEquals(OUTBOUND_NOT_FOUND, exception.getOutboundErrorType());
        }

        @Test
        @Order(3)
        @DisplayName("[Service]출고 상세 삭제 성공")
        void deleteOutboundDetailSuccess() {
            //when
            outboundService.deleteOutboundDetail(3L);
            //then
            OutboundException exception = assertThrows(OutboundException.class,
                    () -> outboundService.deleteOutboundDetail(3L));
            assertEquals(OUTBOUND_DETAILS_NOT_FOUND, exception.getOutboundErrorType());
        }
    }

    //==================== Fail Cases ========================

    //countByStock()
    @Test
    @DisplayName("[Service]재고에 대한 출고 횟수 조회 실패 - (재고 X)")
    void countByStockNotFoundFail(){
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> outboundService.countByStock(10000L));
        //then
        assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
    }

    //getOutboundDetails()
    @Test
    @DisplayName("[Service]출고 상세 조회 실패 - (출고 X)")
    void getOutboundDetailsOutboundNotFoundFail(){
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundService.getOutboundDetails(10000L));
        //then
        assertEquals(OUTBOUND_NOT_FOUND, exception.getOutboundErrorType());
    }

    //addOutbound()
    @Test
    @DisplayName("[Service]출고 추가 실패 - (수량 X)")
    void addOutboundAmountNullFail(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(1L)
                .stocks(Arrays.asList(new OutboundDto.stock(1L, 5D)))
                .amount(null)
                .destination("ccc")
                .note("ddd")
                .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundService.addOutbound(request));
        //then
        assertEquals(OUTBOUND_AMOUNT_NULL, exception.getOutboundErrorType());
    }
    @Test
    @DisplayName("[Service]출고 추가 실패 - (수량 <= 0)")
    void addOutboundAmountOrLessZeroFail(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(1L)
                .stocks(Arrays.asList(new OutboundDto.stock(1L, 5D)))
                .amount(0D)
                .destination("ccc")
                .note("ddd")
                .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundService.addOutbound(request));
        //then
        assertEquals(OUTBOUND_AMOUNT_OR_LESS_ZERO, exception.getOutboundErrorType());
    }
    @Test
    @DisplayName("[Service]출고 추가 실패 - (품목 X)")
    void addOutboundProductNotFoundFail(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(10000L)
                .stocks(Arrays.asList(new OutboundDto.stock(1L, 5D)))
                .amount(5D)
                .destination("ccc")
                .note("ddd")
                .build();
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> outboundService.addOutbound(request));
        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    //addOutboundDetail()
    @Test
    @DisplayName("[Service]출고 상세 추가 실패 - (수량 X)")
    void addOutboundDetailAmountNullFail(){
        //given
        OutboundDto.OutboundDetailAddRequest request =
                OutboundDto.OutboundDetailAddRequest.builder()
                        .outboundId(3L)
                        .stockId(1L)
                        .amount(null)
                        .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundService.addOutboundDetail(request));
        //then
        assertEquals(OUTBOUND_AMOUNT_NULL, exception.getOutboundErrorType());
    }
    @Test
    @DisplayName("[Service]출고 상세 추가 실패 - (수량 <= 0)")
    void addOutboundDetailAmountOrLessZeroFail(){
        //given
        OutboundDto.OutboundDetailAddRequest request =
                OutboundDto.OutboundDetailAddRequest.builder()
                        .outboundId(3L)
                        .stockId(1L)
                        .amount(0D)
                        .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundService.addOutboundDetail(request));
        //then
        assertEquals(OUTBOUND_AMOUNT_OR_LESS_ZERO, exception.getOutboundErrorType());
    }
    @Test
    @DisplayName("[Service]출고 상세 추가 실패 - (출고 X)")
    void addOutboundDetailOutboundNotFoundFail(){
        //given
        OutboundDto.OutboundDetailAddRequest request =
                OutboundDto.OutboundDetailAddRequest.builder()
                        .outboundId(10000L)
                        .stockId(1L)
                        .amount(1D)
                        .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundService.addOutboundDetail(request));
        //then
        assertEquals(OUTBOUND_NOT_FOUND, exception.getOutboundErrorType());
    }
    @Test
    @DisplayName("[Service]출고 상세 추가 실패 - (재고 X)")
    void addOutboundDetailStocksNotFoundFail(){
        //given
        OutboundDto.OutboundDetailAddRequest request =
                OutboundDto.OutboundDetailAddRequest.builder()
                        .outboundId(1L)
                        .stockId(10000L)
                        .amount(1D)
                        .build();
        //when
        StocksException exception = assertThrows(StocksException.class,
                () -> outboundService.addOutboundDetail(request));
        //then
        assertEquals(STOCKS_NOT_FOUND, exception.getStocksErrorType());
    }

    //deleteOutbound()
    @Test
    @DisplayName("[Service]출고 삭제 실패 - (출고 X)")
    void deleteOutboundNotFoundFail(){
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundService.deleteOutbound(10000L));
        //then
        assertEquals(OUTBOUND_NOT_FOUND, exception.getOutboundErrorType());
    }

    //deleteOutboundDetail()
    @Test
    @DisplayName("[Service]출고 상세 삭제 실패 - (출고 상세 X)")
    void deleteOutboundDetailNotFoundFail(){
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundService.deleteOutboundDetail(10000L));
        //then
        assertEquals(OUTBOUND_DETAILS_NOT_FOUND, exception.getOutboundErrorType());
    }
}