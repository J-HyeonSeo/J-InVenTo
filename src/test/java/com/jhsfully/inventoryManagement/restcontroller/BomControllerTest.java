package com.jhsfully.inventoryManagement.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.security.JwtAuthenticationFilter;
import com.jhsfully.inventoryManagement.security.SecurityConfiguration;
import com.jhsfully.inventoryManagement.service.BomInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.support.NullValue;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = BomController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfiguration.class
, JwtAuthenticationFilter.class}))
class BomControllerTest {

    @MockBean
    BomInterface bomService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("[Controller]Bom목록 가져오기 성공")
    void getBomsSuccess() throws Exception {
        //given
        given(bomService.getBoms())
                .willReturn(new ArrayList<BomDto.BomTopResponse>(
                        Arrays.asList(
                                BomDto.BomTopResponse.builder()
                                        .productId(1L)
                                        .productName("a")
                                        .build()
                        )
                ));

        //when & then
        mockMvc.perform(get("/bom"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("a"));
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]Bom 가져오기 성공")
    void getBomSuccess() throws Exception {
        //given
        given(bomService.getBom(anyLong()))
                .willReturn(BomDto.BomTreeResponse.builder()
                        .id(null)
                        .productName("a")
                        .productId(1L)
                        .children(null)
                        .cost(10D)
                        .build());

        //when & then
        mockMvc.perform(get("/bom/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("a"))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.cost").value(10D));
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]BomLeafProducts 가져오기 성공")
    void getLeafProductsSuccess() throws Exception {
        //given
        given(bomService.getLeafProducts(anyLong()))
                .willReturn(new ArrayList<BomDto.BomLeaf>(
                        Arrays.asList(
                                new BomDto.BomLeaf(1L, "a", 1D)
                        )
                ));
        //when & then
        mockMvc.perform(get("/bom/leaf/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("a"))
                .andExpect(jsonPath("$[0].cost").value(1D));
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]Bom 추가 성공")
    void addBomSuccess() throws Exception {
        //given
        BomDto.BomAddRequest request = BomDto.BomAddRequest.builder().build();

        BomDto.BomResponse response = BomDto.BomResponse.builder()
                .id(1L)
                .pid(1L)
                .parentName("a")
                .cid(2L)
                .childName("b")
                .cost(100D)
                .build();

        given(bomService.addBom(any()))
                .willReturn(response);
        //when & then
        mockMvc.perform(post("/bom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.pid").value(1L))
                .andExpect(jsonPath("$.parentName").value("a"))
                .andExpect(jsonPath("$.cid").value(2L))
                .andExpect(jsonPath("$.childName").value("b"))
                .andExpect(jsonPath("$.cost").value(100D));

    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]BomNode 삭제 성공")
    void deleteBomNodeSuccess() throws Exception {
        //when & then
        mockMvc.perform(delete("/bom/node/123").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("123"));

        verify(bomService, times(1)).deleteBomNode(123L);
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]BomTree 삭제 성공")
    void deleteBomTreeSuccess() throws Exception {
        //when & then
        mockMvc.perform(delete("/bom/tree/123").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("123"));

        verify(bomService, times(1)).deleteBomTree(123L);
    }

}