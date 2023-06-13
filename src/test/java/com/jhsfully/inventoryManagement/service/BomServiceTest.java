package com.jhsfully.inventoryManagement.service;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BomServiceTest {

    @BeforeAll
    static void setup(@Autowired DataSource dataSource){
        try(Connection conn = dataSource.getConnection()){
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/product.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/bom.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    BomService bomService;

    //=================== Success Cases =========================
    @Test
    @DisplayName("[Service]Bom들 가져오기 성공")
    void getBomsSuccess(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]BomTree 가져오기 성공")
    void getBomSuccess(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]하단 품목 가져오기 성공")
    void getLeafProductsSuccess(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]Bom 추가 성공")
    void addBomSuccess(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]Bom 수정 성공")
    void updateBomSuccess(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]BomNode 삭제 성공")
    void deleteBomNodeSuccess(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]BomTree 삭제 성공")
    void deleteBomTreeSuccess(){
        //given
        //when
        //then
    }


    //=================== Fail Cases =========================

    //getBom()
    @Test
    @DisplayName("[Service]Bom 가져오기 실패 - (품목 X)")
    void getBomProductNotFoundFail(){
        //given
        //when
        //then
    }

    //getLeafProducts()
    @Test
    @DisplayName("[Service]BomLeafProducts 가져오기 실패 - (품목 X)")
    void getBomLeafProductsProductNotFoundFail(){
        //given
        //when
        //then
    }

    //addBom(request)
    @Test
    @DisplayName("[Service]Bom 추가 실패 - (Cost X)")
    void addBomCostNullFail(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]Bom 추가 실패 - (Cost -)")
    void addBomCostMinusFail(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]Bom 추가 실패 - (Parent Product X)")
    void addBomParentProductNotFoundFail(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]Bom 추가 실패 - (Child Product X)")
    void addBomChildProductNotFoundFail(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("[Service]Bom 추가 실패 - (같은 부모)")
    void addBomHasSameParentFail(){
        //given
        //when
        //then
    }

    //updateBom()
    @Test
    @DisplayName("[Service]Bom 수정 실패 - (Cost X)")
    void updateBomCostNullFail(){
        //given
        //when
        //then
    }
    @Test
    @DisplayName("[Service]Bom 수정 실패 - (Cost -)")
    void updateBomCostMinusFail(){
        //given
        //when
        //then
    }
    @Test
    @DisplayName("[Service]Bom 수정 실패 - (BOM X)")
    void updateBomBomNotFoundFail(){
        //given
        //when
        //then
    }

    //deleteBomNode()
    @Test
    @DisplayName("[Service]BomNode 삭제 실패 - (BOM X)")
    void deleteBomNodeFail(){
        //given
        //when
        //then
    }

    //deleteBomTree()
    @Test
    @DisplayName("[Service]BomTree 삭제 실패 - (품목 X)")
    void deleteBomTreeFail(){
        //given
        //when
        //then
    }
}