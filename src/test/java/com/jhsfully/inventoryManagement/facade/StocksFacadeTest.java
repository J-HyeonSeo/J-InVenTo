package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.type.CacheType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class StocksFacadeTest {

    @BeforeAll
    static void setup(@Autowired DataSource dataSource){
        try(Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/product.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/bom.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/purchase.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/stocks.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/inbound.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/plan.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    CacheManager redisCacheManager;

    @BeforeEach
    void evictCache(){
        redisCacheManager.getCache(CacheType.BOM_LEAF).clear();
        redisCacheManager.getCache(CacheType.BOM_TREE).clear();
        redisCacheManager.getCache(CacheType.BOM_TOP).clear();
        redisCacheManager.getCache(CacheType.ENABLE_PRODUCTS).clear();
    }

    @Autowired
    private StocksFacade stocksFacade;

    @Test
    @DisplayName("[Facade]모든 재고 가져오기")
    void getAllStocks(){
        //when
        List<StocksDto.StockResponse> allStocks = stocksFacade.getAllStocks();

        for(var stocks : allStocks){

            System.out.println("===============================");
            System.out.println(stocks.getProductName());
            System.out.println(stocks.getAmount());
            System.out.println(stocks.getPrice());
            System.out.println(stocks.getSpec());
            System.out.println(stocks.getLackAmount());
            System.out.println(stocks.getLackDate());

        }

        //then
        assertAll(
                () -> assertEquals(9, allStocks.size()),
                () -> assertEquals(1, allStocks.get(0).getProductId()),
                    () -> assertEquals(1400000, allStocks.get(0).getPrice()),
                    () -> assertEquals(20, allStocks.get(0).getAmount()),
                    () -> assertEquals(LocalDate.of(2023, 6, 18), allStocks.get(0).getLackDate()),
                    () -> assertEquals(80, allStocks.get(0).getLackAmount()),

                () -> assertEquals(3, allStocks.get(1).getProductId()),
                    () -> assertEquals(0, allStocks.get(1).getAmount()),

                () -> assertEquals(4, allStocks.get(2).getProductId()),
                    () -> assertEquals(1, allStocks.get(2).getAmount()),
                    () -> assertEquals(LocalDate.of(2023, 6, 21), allStocks.get(2).getLackDate()),
                    () -> assertEquals(299, allStocks.get(2).getLackAmount()),

                () -> assertEquals(5, allStocks.get(3).getProductId()),
                    () -> assertEquals(1, allStocks.get(3).getAmount()),
                    () -> assertEquals(LocalDate.of(2023, 6, 21), allStocks.get(3).getLackDate()),
                    () -> assertEquals(299, allStocks.get(3).getLackAmount()),

                () -> assertEquals(6, allStocks.get(4).getProductId()),
                    () -> assertEquals(0, allStocks.get(4).getAmount()),
                    () -> assertEquals(null, allStocks.get(4).getLackDate()),
                    () -> assertEquals(0, allStocks.get(4).getLackAmount()),

                () -> assertEquals(7, allStocks.get(5).getProductId()),
                    () -> assertEquals(1, allStocks.get(5).getAmount()),
                    () -> assertEquals(LocalDate.of(2023, 6, 21), allStocks.get(5).getLackDate()),
                    () -> assertEquals(299, allStocks.get(5).getLackAmount()),

                () -> assertEquals(8, allStocks.get(6).getProductId()),
                    () -> assertEquals(0, allStocks.get(6).getAmount()),
                    () -> assertEquals(null, allStocks.get(6).getLackDate()),
                    () -> assertEquals(0, allStocks.get(6).getLackAmount()),
                () -> assertEquals(9, allStocks.get(7).getProductId()),
                    () -> assertEquals(0, allStocks.get(7).getAmount()),
                    () -> assertEquals(null, allStocks.get(7).getLackDate()),
                    () -> assertEquals(0, allStocks.get(7).getLackAmount()),
                () -> assertEquals(10, allStocks.get(8).getProductId())
        );
    }

}