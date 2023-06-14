package com.jhsfully.inventoryManagement.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.security.JwtAuthenticationFilter;
import com.jhsfully.inventoryManagement.security.SecurityConfiguration;
import com.jhsfully.inventoryManagement.service.PurchaseInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PurchaseController.class, excludeFilters =
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
            classes = {SecurityConfiguration.class, JwtAuthenticationFilter.class}))
class PurchaseControllerTest {

    @MockBean
    PurchaseInterface purchaseService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("[Controller] 구매 목록 가져오기 성공")
    void getPurchasesSuccess() throws Exception {
        //given
        given(purchaseService.getPurchases(any(), any()))
                .willReturn(new ArrayList<PurchaseDto.PurchaseResponse>(
                        Arrays.asList(
                                PurchaseDto.PurchaseResponse.builder()
                                        .id(1L)
                                        .productName("a")
                                        .company("b")
                                        .amount(1D)
                                        .price(1D)
                                        .at(LocalDateTime.of(2020, 1, 1,
                                                1, 1, 1))
                                        .note("c")
                                        .build()
                        )
                ));
        //when & then
        mockMvc.perform(get("/purchase?startDate=20200101&endDate=20200101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].productName").value("a"))
                .andExpect(jsonPath("$[0].company").value("b"))
                .andExpect(jsonPath("$[0].amount").value(1D))
                .andExpect(jsonPath("$[0].price").value(1D))
                .andExpect(jsonPath("$[0].at").value(LocalDateTime.of(
                        2020, 1, 1, 1, 1, 1
                ).toString()))
                .andExpect(jsonPath("$[0].note").value("c"));
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller] 구매 추가 성공")
    void addPurchaseSuccess() throws Exception {
        //given
        given(purchaseService.addPurchase(any()))
                .willReturn(PurchaseDto.PurchaseResponse.builder()
                        .id(1L)
                        .productId(1L)
                        .productName("a")
                        .company("b")
                        .price(1D)
                        .amount(1D)
                        .at(LocalDateTime.of(
                                2020, 1, 1 ,1 ,1 ,1
                        ))
                        .note("c")
                        .build());
        PurchaseDto.PurchaseAddRequest request = PurchaseDto.PurchaseAddRequest.builder().build();
        //when & then
        mockMvc.perform(post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.productName").value("a"))
                .andExpect(jsonPath("$.company").value("b"))
                .andExpect(jsonPath("$.price").value(1D))
                .andExpect(jsonPath("$.amount").value(1D))
                .andExpect(jsonPath("$.at").value(LocalDateTime.of(
                        2020, 1, 1 , 1, 1, 1
                        ).toString()
                ))
                .andExpect(jsonPath("$.note").value("c"));
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller] 구매 삭제 성공")
    void deletePurchaseSuccess() throws Exception {
        //when & then
        mockMvc.perform(delete("/purchase/123").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("123"));
        verify(purchaseService, times(1)).deletePurchase(123L);
    }

}