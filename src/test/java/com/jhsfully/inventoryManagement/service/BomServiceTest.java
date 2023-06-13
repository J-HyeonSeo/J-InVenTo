package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.entity.BOMEntity;
import com.jhsfully.inventoryManagement.exception.BomException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.repository.BomRepository;
import com.jhsfully.inventoryManagement.type.BomErrorType;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
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
import java.util.List;

import static com.jhsfully.inventoryManagement.type.BomErrorType.*;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BomServiceTest {

    @BeforeAll
    static void setup(@Autowired DataSource dataSource){
        try(Connection conn = dataSource.getConnection()){
            //given
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/product.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/bom.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    BomService bomService;

    @Autowired
    BomRepository bomRepository;

    //=================== Success Cases =========================
    @Test
    @Order(1)
    @DisplayName("[Service]Bom들 가져오기 성공")
    void getBomsSuccess(){
        //when
        List<BomDto.BomTopResponse> boms = bomService.getBoms();
        //then
        assertEquals(2, boms.size());
    }

    @Test
    @Order(4)
    @DisplayName("[Service]BomTree 가져오기 성공")
    void getBomSuccess(){
        //when
        BomDto.BomTreeResponse bom = bomService.getBom(3L);
        //then
        assertAll(
                () -> assertEquals(3, bom.getChildren().size()),
                () -> assertEquals(4, bom.getChildren().get(0).getProductId()),
                () -> assertEquals(5, bom.getChildren().get(1).getProductId()),
                () -> assertEquals(6, bom.getChildren().get(2).getProductId()),
                () -> assertEquals(1, bom.getChildren().get(2).getChildren().size()),
                () -> assertEquals(7, bom.getChildren().get(2).getChildren().get(0).getProductId())
        );
    }

    @Test
    @Order(5)
    @DisplayName("[Service]하단 품목 가져오기 성공")
    void getLeafProductsSuccess(){
        //when
        List<BomDto.BomLeaf> leafProducts = bomService.getLeafProducts(3L);
        //then
        assertAll(
                () -> assertEquals(3, leafProducts.size()),
                () -> assertEquals(4, leafProducts.get(0).getProductId()),
                () -> assertEquals(5, leafProducts.get(1).getProductId()),
                () -> assertEquals(7, leafProducts.get(2).getProductId())
        );
    }

    @Test
    @DisplayName("[Service]Bom 추가 성공")
    @Order(2)
    void addBomSuccess(){
        //given
        BomDto.BomAddRequest request = BomDto.BomAddRequest.builder()
                .pid(8L)
                .cid(9L)
                .cost(5.5D)
                .build();

        //when
        Long bomID = bomService.addBom(request).getId();
        //then
        BOMEntity bomEntity = bomRepository.findById(bomID).orElseThrow();

        assertAll(
                () -> assertEquals(request.getPid(), bomEntity.getParentProduct().getId()),
                () -> assertEquals(request.getCid(), bomEntity.getChildProduct().getId()),
                () -> assertEquals(request.getCost(), bomEntity.getCost())
        );
    }

    @Test
    @Order(3)
    @DisplayName("[Service]Bom 수정 성공")
    void updateBomNodeSuccess(){
        //given
        BomDto.BomUpdateRequest request = BomDto.BomUpdateRequest.builder()
                .id(5L)
                .cost(6D)
                .build();

        //when
        bomService.updateBomNode(request);

        //then
        BOMEntity bomEntity = bomRepository.findById(5L).orElseThrow();

        assertAll(
                () -> assertEquals(request.getCost(), bomEntity.getCost())
        );
    }

    @Test
    @Order(6)
    @Transactional
    @DisplayName("[Service]BomNode 삭제 성공")
    void deleteBomNodeSuccess(){
        //when
        bomService.deleteBomNode(5L);
        //then
        BomException exception = assertThrows(BomException.class,
                () -> bomService.deleteBomNode(5L));
        assertEquals(BOM_NOT_FOUND, exception.getBomErrorType());
    }

    @Test
    @Order(7)
    @Transactional
    @DisplayName("[Service]BomTree 삭제 성공")
    void deleteBomTreeSuccess(){
        //when
        bomService.deleteBomTree(3L);
        //then
        BomDto.BomTreeResponse bom = bomService.getBom(3L);
        assertEquals(0, bom.getChildren().size());
    }


    //=================== Fail Cases =========================

    //getBom()
    @Test
    @DisplayName("[Service]Bom 가져오기 실패 - (품목 X)")
    void getBomProductNotFoundFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> bomService.getBom(10000L));
        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    //getLeafProducts()
    @Test
    @DisplayName("[Service]BomLeafProducts 가져오기 실패 - (품목 X)")
    void getBomLeafProductsProductNotFoundFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> bomService.getLeafProducts(10000L));
        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    //addBom(request)
    @Test
    @DisplayName("[Service]Bom 추가 실패 - (Cost X)")
    void addBomCostNullFail(){
        //given
        BomDto.BomAddRequest request = BomDto.BomAddRequest.builder()
                .pid(8L)
                .cid(9L)
                .cost(null)
                .build();
        //when
        BomException exception = assertThrows(BomException.class,
                () -> bomService.addBom(request));
        //then
        assertEquals(COST_NULL, exception.getBomErrorType());
    }

    @Test
    @DisplayName("[Service]Bom 추가 실패 - (Cost -)")
    void addBomCostMinusFail(){
        //given
        BomDto.BomAddRequest request = BomDto.BomAddRequest.builder()
                .pid(8L)
                .cid(9L)
                .cost(0D)
                .build();
        //when
        BomException exception = assertThrows(BomException.class,
                () -> bomService.addBom(request));
        //then
        assertEquals(COST_MINUS, exception.getBomErrorType());
    }

    @Test
    @DisplayName("[Service]Bom 추가 실패 - (Parent Product X)")
    void addBomParentProductNotFoundFail(){
        //given
        BomDto.BomAddRequest request = BomDto.BomAddRequest.builder()
                .pid(1000L)
                .cid(9L)
                .cost(1000D)
                .build();
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> bomService.addBom(request));
        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]Bom 추가 실패 - (Child Product X)")
    void addBomChildProductNotFoundFail(){
        //given
        BomDto.BomAddRequest request = BomDto.BomAddRequest.builder()
                .pid(8L)
                .cid(10000L)
                .cost(1000D)
                .build();
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> bomService.addBom(request));
        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }

    @Test
    @DisplayName("[Service]Bom 추가 실패 - (같은 부모)")
    void addBomHasSameParentFail(){
        //given
        BomDto.BomAddRequest request = BomDto.BomAddRequest.builder()
                .pid(3L)
                .cid(6L)
                .cost(1000D)
                .build();
        //when
        BomException exception = assertThrows(BomException.class,
                () -> bomService.addBom(request));
        //then
        assertEquals(HAS_SAME_PARENT, exception.getBomErrorType());
    }

    //updateBom()
    @Test
    @DisplayName("[Service]Bom 수정 실패 - (Cost X)")
    void updateBomCostNullFail(){
        //given
        BomDto.BomUpdateRequest request = BomDto.BomUpdateRequest.builder()
                .id(5L)
                .cost(null)
                .build();

        //when
        BomException exception = assertThrows(BomException.class,
                () -> bomService.updateBomNode(request));
        //then
        assertEquals(COST_NULL, exception.getBomErrorType());
    }
    @Test
    @DisplayName("[Service]Bom 수정 실패 - (Cost -)")
    void updateBomCostMinusFail(){
        //given
        BomDto.BomUpdateRequest request = BomDto.BomUpdateRequest.builder()
                .id(5L)
                .cost(-0D)
                .build();

        //when
        BomException exception = assertThrows(BomException.class,
                () -> bomService.updateBomNode(request));
        //then
        assertEquals(COST_MINUS, exception.getBomErrorType());
    }
    @Test
    @DisplayName("[Service]Bom 수정 실패 - (BOM X)")
    void updateBomBomNotFoundFail(){
        //given
        BomDto.BomUpdateRequest request = BomDto.BomUpdateRequest.builder()
                .id(10000L)
                .cost(100D)
                .build();

        //when
        BomException exception = assertThrows(BomException.class,
                () -> bomService.updateBomNode(request));
        //then
        assertEquals(BOM_NOT_FOUND, exception.getBomErrorType());
    }

    //deleteBomNode()
    @Test
    @DisplayName("[Service]BomNode 삭제 실패 - (BOM X)")
    void deleteBomNodeFail(){
        //when
        BomException exception = assertThrows(BomException.class,
                () -> bomService.deleteBomNode(10000L));
        //then
        assertEquals(BOM_NOT_FOUND, exception.getBomErrorType());
    }

    //deleteBomTree()
    @Test
    @DisplayName("[Service]BomTree 삭제 실패 - (품목 X)")
    void deleteBomTreeFail(){
        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> bomService.deleteBomTree(10000L));
        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }
}