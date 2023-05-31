package com.jhsfully.inventoryManagement.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[Controller]전품목 가져오기 테스트")
    void getProductsTest() throws Exception {
        //given
        given(productService.getProducts())
                .willReturn(new ArrayList<>(Arrays.asList(
                        ProductDto.ProductResponse
                                .builder()
                                .id(1)
                                .name("testProduct")
                                .company("testCompany")
                                .price(2500)
                                .spec("testSpec")
                                .build()
                )));
        //when & then
        mockMvc.perform(get("/product"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("testProduct"))
                .andExpect(jsonPath("$[0].company").value("testCompany"))
                .andExpect(jsonPath("$[0].price").value(2500))
                .andExpect(jsonPath("$[0].spec").value("testSpec"));
    }

    @Test
    @DisplayName("[Controller]품목 등록 테스트")
    void addProductTest() throws Exception {
        //given
        ProductDto.ProductResponse obj = ProductDto.ProductResponse
                                                        .builder()
                                                        .id(1)
                                                        .name("testProduct")
                                                        .company("testCompany")
                                                        .price(2500)
                                                        .spec("testSpec")
                                                        .build();

        given(productService.addProduct(any()))
                .willReturn(obj);

        //when & then
        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(obj)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("testProduct"))
                .andExpect(jsonPath("$.company").value("testCompany"))
                .andExpect(jsonPath("$.price").value(2500))
                .andExpect(jsonPath("$.spec").value("testSpec"));
    }


    @Test
    @DisplayName("[Controller]품목 수정 테스트")
    void updateProductTest() throws Exception {
        //given
        ProductDto.ProductResponse obj = ProductDto.ProductResponse
                                                .builder()
                                                .id(1)
                                                .name("testProduct")
                                                .company("testCompany")
                                                .price(2500)
                                                .spec("testSpec")
                                                .build();
        given(productService.updateProduct(any()))
                .willReturn(obj);

        //when & then
        mockMvc.perform(put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("testProduct"))
                .andExpect(jsonPath("$.company").value("testCompany"))
                .andExpect(jsonPath("$.price").value(2500))
                .andExpect(jsonPath("$.spec").value("testSpec"));
    }

    @Test
    @DisplayName("[Controller]품목 제거 테스트")
    void deleteProductTest() throws Exception {
        //given
        //when & then
        mockMvc.perform(delete("/product/123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("123"));
        verify(productService).deleteProduct(anyLong());
    }
}