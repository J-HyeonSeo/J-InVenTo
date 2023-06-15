package com.jhsfully.inventoryManagement.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.facade.OutboundFacade;
import com.jhsfully.inventoryManagement.security.JwtAuthenticationFilter;
import com.jhsfully.inventoryManagement.security.SecurityConfiguration;
import com.jhsfully.inventoryManagement.service.OutboundInterface;
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
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = OutboundController.class, excludeFilters =
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
            {SecurityConfiguration.class, JwtAuthenticationFilter.class}))
class OutboundControllerTest {

    @MockBean
    private OutboundFacade outboundFacade;

    @MockBean
    private OutboundInterface outboundService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("[Controller]출고 가져오기 성공")
    void getOutboundsSuccess() throws Exception {
        //given
        given(outboundService.getOutbounds(any(), any()))
                .willReturn(Arrays.asList(
                        OutboundDto.OutboundResponse.builder()
                                .id(1L)
                                .productName("a")
                                .amount(1D)
                                .price(1D)
                                .destination("b")
                                .at(LocalDateTime.of(2023,1,1,1,1,1))
                                .note("c")
                                .build()
                ));
        //when & then
        mockMvc.perform(get("/outbound?startDate=20230101&endDate=20230101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].productName").value("a"))
                .andExpect(jsonPath("$[0].amount").value(1D))
                .andExpect(jsonPath("$[0].price").value(1D))
                .andExpect(jsonPath("$[0].destination").value("b"))
                .andExpect(jsonPath("$[0].at").value(
                        LocalDateTime.of(2023,1,1,1,1,1)
                                .toString()
                ))
                .andExpect(jsonPath("$[0].note").value("c"));
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]출고 상세 가져오기 성공")
    void getOutboundDetailsSuccess() throws Exception {
        //given
        given(outboundService.getOutboundDetails(anyLong()))
                .willReturn(Arrays.asList(
                        OutboundDto.OutboundDetailResponse.builder()
                                .id(1L)
                                .stockId(1L)
                                .productName("a")
                                .company("b")
                                .amount(1D)
                                .price(1D)
                                .build()
                ));
        //when & then
        mockMvc.perform(get("/outbound/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].stockId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("a"))
                .andExpect(jsonPath("$[0].company").value("b"))
                .andExpect(jsonPath("$[0].price").value(1D))
                .andExpect(jsonPath("$[0].amount").value(1D));
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]출고 수행 성공")
    void executeOutboundSuccess() throws Exception {
        //given
        OutboundDto.OutboundAddRequest request = OutboundDto.OutboundAddRequest.builder().build();
        given(outboundFacade.executeOutbound(any()))
                .willReturn(1L);

        //when & then
        mockMvc.perform(post("/outbound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(outboundFacade, times(1)).executeOutbound(any());
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]출고 취소 성공")
    void cancelOutboundSuccess() throws Exception {
        //when & then
        mockMvc.perform(delete("/outbound/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(outboundFacade, times(1)).cancelOutbound(any());
    }

    @Test
    @WithMockUser
    @DisplayName("[Controller]출고 상세 취소 성공")
    void cancelOutboundDetailSuccess() throws Exception {
        //when & then
        mockMvc.perform(delete("/outbound/detail/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(outboundFacade, times(1)).cancelOutboundDetail(any());
    }

}