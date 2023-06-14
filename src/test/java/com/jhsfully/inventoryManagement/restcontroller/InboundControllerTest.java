package com.jhsfully.inventoryManagement.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.facade.InboundFacade;
import com.jhsfully.inventoryManagement.security.JwtAuthenticationFilter;
import com.jhsfully.inventoryManagement.security.SecurityConfiguration;
import com.jhsfully.inventoryManagement.service.InboundInterface;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = InboundController.class, excludeFilters =
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
            SecurityConfiguration.class, JwtAuthenticationFilter.class
    }))
class InboundControllerTest {

    @MockBean
    private InboundFacade inboundFacade;

    @MockBean
    private InboundInterface inboundService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("[Controller]입고 목록 가져오기 성공")
    void getInboundsSuccess() throws Exception {
        //given
        given(inboundService.getInbounds(any(), any()))
                .willReturn(new ArrayList<>(
                        Arrays.asList(
                                InboundDto.InboundResponse
                                        .builder()
                                        .id(1L)
                                        .purchaseId(1L)
                                        .stockId(1L)
                                        .productName("a")
                                        .purchasedAt(LocalDateTime.of(2023,1,1,1,1,1))
                                        .inboundAt(LocalDateTime.of(2023,1,1,1,1,1))
                                        .company("c")
                                        .amount(10D)
                                        .price(70000D)
                                        .note("d")
                                        .build()
                        )
                ));
        //when & then
        mockMvc.perform(get("/inbound?startDate=20230101&endDate=20230101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].purchaseId").value(1L))
                .andExpect(jsonPath("$[0].stockId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("a"))
                .andExpect(jsonPath("$[0].purchasedAt").value(
                        LocalDateTime.of(2023,1,1,1,1,1)
                                .toString()
                ))
                .andExpect(jsonPath("$[0].inboundAt").value(
                        LocalDateTime.of(2023,1,1,1,1,1)
                                .toString()
                ))
                .andExpect(jsonPath("$[0].company").value("c"))
                .andExpect(jsonPath("$[0].amount").value(10D))
                .andExpect(jsonPath("$[0].price").value(70000D))
                .andExpect(jsonPath("$[0].note").value("d"));
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]입고합 가져오기 By 구매 성공")
    void getSumInboundsByPurchaseSuccess() throws Exception {
        //given
        given(inboundService.getInboundsByPurchase(anyLong()))
                .willReturn(100D);
        //when & then
        mockMvc.perform(get("/inbound/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]입고 수행 성공")
    void executeInboundSuccess() throws Exception {
        //given
        given(inboundFacade.executeInbound(any()))
                .willReturn(1L);

        InboundDto.InboundOuterAddRequest request = InboundDto.InboundOuterAddRequest
                .builder().build();

        //when & then
        mockMvc.perform(post("/inbound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(inboundFacade, times(1)).executeInbound(any());

    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]입고 취소 성공")
    void cancelInboundSuccess() throws Exception {

        //when & then
        mockMvc.perform(delete("/inbound/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(inboundFacade, times(1)).cancelInbound(anyLong());

    }
}