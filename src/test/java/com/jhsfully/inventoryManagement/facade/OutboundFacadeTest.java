package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.entity.OutboundDetailsEntity;
import com.jhsfully.inventoryManagement.entity.OutboundEntity;
import com.jhsfully.inventoryManagement.exception.OutboundException;
import com.jhsfully.inventoryManagement.repository.OutboundDetailRepository;
import com.jhsfully.inventoryManagement.repository.OutboundRepository;
import com.jhsfully.inventoryManagement.service.OutboundInterface;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import com.jhsfully.inventoryManagement.type.CacheType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static com.jhsfully.inventoryManagement.type.OutboundErrorType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OutboundFacadeTest {

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
    private CacheManager redisCacheManager;

    private Cache redisCache; // 특정 Cache를 지우기 위한 변수

    @BeforeEach
    public void clearCache() {
        redisCacheManager.getCache(CacheType.BOM_LEAF).clear();
        redisCacheManager.getCache(CacheType.ENABLE_PRODUCTS).clear();
    }


    @Autowired
    OutboundFacade outboundFacade;

    @Autowired
    OutboundInterface outboundService;

    @Autowired
    StocksInterface stocksService;

    @Autowired
    OutboundRepository outboundRepository;

    @Autowired
    OutboundDetailRepository outboundDetailRepository;

    //================= Success Cases ======================

    @Test
    @Order(1)
    @DisplayName("[Facade]출고 수행 성공")
    void executeOutboundSuccess(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(3L)
                .amount(1D)
                .stocks(Arrays.asList(
                        new OutboundDto.stock(4L, 1D),
                        new OutboundDto.stock(5L, 1D),
                        new OutboundDto.stock(6L, 1D)
                ))
                .note("자동차출고")
                .destination("destination")
                .build();
        //when
        Long outboundId = outboundFacade.executeOutbound(request);
        //then
        OutboundEntity outboundEntity = outboundRepository.findById(outboundId).orElseThrow();
        List<OutboundDetailsEntity> detailsEntities = outboundDetailRepository.findByOutbound(outboundEntity);
        StocksDto.StockResponseLot stock4L = stocksService.getStock(4L);
        StocksDto.StockResponseLot stock5L = stocksService.getStock(5L);
        StocksDto.StockResponseLot stock6L = stocksService.getStock(6L);
        assertAll(
                () -> assertEquals(3L, outboundEntity.getProduct().getId()),
                () -> assertEquals(1D, outboundEntity.getAmount()),
                () -> assertEquals("destination", outboundEntity.getDestination()),
                () -> assertEquals("자동차출고", outboundEntity.getNote()),
                () -> assertEquals(3, detailsEntities.size()),
                () -> assertEquals(4L, detailsEntities.get(0).getStock().getProduct().getId()),
                () -> assertEquals(5L, detailsEntities.get(1).getStock().getProduct().getId()),
                () -> assertEquals(7L, detailsEntities.get(2).getStock().getProduct().getId()),
                () -> assertEquals(outboundId, detailsEntities.get(0).getOutbound().getId()),
                () -> assertEquals(outboundId, detailsEntities.get(1).getOutbound().getId()),
                () -> assertEquals(outboundId, detailsEntities.get(2).getOutbound().getId()),
                () -> assertEquals(1D, detailsEntities.get(0).getAmount()),
                () -> assertEquals(1D, detailsEntities.get(1).getAmount()),
                () -> assertEquals(1D, detailsEntities.get(2).getAmount()),
                () -> assertEquals(0D, stock4L.getAmount()),
                () -> assertEquals(0D, stock5L.getAmount()),
                () -> assertEquals(0D, stock6L.getAmount())
        );

    }

    @Test
    @Order(3)
    @DisplayName("[Facade]출고 취소 성공")
    void cancelOutboundSuccess(){
        //when
        outboundFacade.cancelOutbound(3L);
        //then
        OutboundException exceptionO3 = assertThrows(OutboundException.class,
                () -> outboundService.deleteOutbound(3L));
        OutboundException exceptionD3 = assertThrows(OutboundException.class,
                () -> outboundService.deleteOutboundDetail(3L));
        OutboundException exceptionD4 = assertThrows(OutboundException.class,
                () -> outboundService.deleteOutboundDetail(4L));
        StocksDto.StockResponseLot stock4L = stocksService.getStock(4L);
        StocksDto.StockResponseLot stock5L = stocksService.getStock(5L);

        assertAll(
                () -> assertEquals(OUTBOUND_NOT_FOUND, exceptionO3.getOutboundErrorType()),
                () -> assertEquals(OUTBOUND_DETAILS_NOT_FOUND, exceptionD3.getOutboundErrorType()),
                () -> assertEquals(OUTBOUND_DETAILS_NOT_FOUND, exceptionD4.getOutboundErrorType()),
                () -> assertEquals(1D, stock4L.getAmount()),
                () -> assertEquals(1D, stock5L.getAmount())
        );
    }

    @Test
    @Order(2)
    @DisplayName("[Facade]출고 상세 취소 성공")
    void cancelOutboundDetailSuccess(){
        //when
        outboundFacade.cancelOutboundDetail(5L);
        //then
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundService.deleteOutboundDetail(5L));
        StocksDto.StockResponseLot stock = stocksService.getStock(6L);

        assertAll(
                () -> assertEquals(OUTBOUND_DETAILS_NOT_FOUND, exception.getOutboundErrorType()),
                () -> assertEquals(1D, stock.getAmount())
        );
    }

    //===================== Fail Cases ========================

    @Test
    @DisplayName("[Facade]출고 수행 실패 - (수량 X)")
    void executeOutboundAmountNullFail(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(3L)
                .amount(null)
                .stocks(Arrays.asList(
                        new OutboundDto.stock(4L, 1D),
                        new OutboundDto.stock(5L, 1D),
                        new OutboundDto.stock(6L, 1D)
                ))
                .note("자동차출고")
                .destination("destination")
                .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundFacade.executeOutbound(request));
        assertEquals(OUTBOUND_AMOUNT_NULL, exception.getOutboundErrorType());
    }
    @Test
    @DisplayName("[Facade]출고 수행 실패 - (수량 <= 0)")
    void executeOutboundAmountOrLessZeroFail(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(3L)
                .amount(0D)
                .stocks(Arrays.asList(
                        new OutboundDto.stock(4L, 1D),
                        new OutboundDto.stock(5L, 1D),
                        new OutboundDto.stock(6L, 1D)
                ))
                .note("자동차출고")
                .destination("destination")
                .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundFacade.executeOutbound(request));
        assertEquals(OUTBOUND_AMOUNT_OR_LESS_ZERO, exception.getOutboundErrorType());
    }
    @Test
    @DisplayName("[Facade]출고 수행 실패 - (상세 수량 X)")
    void executeOutboundDetailAmountNullFail(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(3L)
                .amount(1D)
                .stocks(Arrays.asList(
                        new OutboundDto.stock(4L, null),
                        new OutboundDto.stock(5L, 1D),
                        new OutboundDto.stock(6L, null)
                ))
                .note("자동차출고")
                .destination("destination")
                .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundFacade.executeOutbound(request));
        assertEquals(OUTBOUND_AMOUNT_NULL, exception.getOutboundErrorType());
    }
    @Test
    @DisplayName("[Facade]출고 수행 실패 - (상세 수량 <= 0)")
    void executeOutboundDetailAmountOrLessZeroFail(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(3L)
                .amount(1D)
                .stocks(Arrays.asList(
                        new OutboundDto.stock(4L, 0D),
                        new OutboundDto.stock(5L, 0D),
                        new OutboundDto.stock(6L, 0D)
                ))
                .note("자동차출고")
                .destination("destination")
                .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundFacade.executeOutbound(request));
        assertEquals(OUTBOUND_AMOUNT_OR_LESS_ZERO, exception.getOutboundErrorType());
    }

    @Test
    @DisplayName("[Facade]출고 수행 실패 - (출고 수량 > 재고 수량)")
    void executeOutboundExceedStockFail(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(3L)
                .amount(2D)
                .stocks(Arrays.asList(
                        new OutboundDto.stock(4L, 2D),
                        new OutboundDto.stock(5L, 2D),
                        new OutboundDto.stock(6L, 2D)
                ))
                .note("자동차출고")
                .destination("destination")
                .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundFacade.executeOutbound(request));
        assertEquals(OUTBOUND_EXCEED_STOCKS, exception.getOutboundErrorType());
    }

    @Test
    @DisplayName("[Facade]출고 수행 실패 - (출고 수량 != BOM Tree 수량)")
    void executeOutboundDifferentRequiresFail(){
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest
                .builder()
                .productId(1L)
                .amount(3D)
                .stocks(Arrays.asList(
                        new OutboundDto.stock(1L, 4D)
                ))
                .note("자동차출고")
                .destination("destination")
                .build();
        //when
        OutboundException exception = assertThrows(OutboundException.class,
                () -> outboundFacade.executeOutbound(request));
        assertEquals(OUTBOUND_DIFFERENT_REQUIRES, exception.getOutboundErrorType());
    }
}