package com.jhsfully.inventoryManagement.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.facade.StocksFacade;
import com.jhsfully.inventoryManagement.security.JwtAuthenticationFilter;
import com.jhsfully.inventoryManagement.security.SecurityConfiguration;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = StocksController.class, excludeFilters =
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
            SecurityConfiguration.class, JwtAuthenticationFilter.class
    }))
class StocksControllerTest {

    @MockBean
    StocksFacade stocksFacade;

    @MockBean
    StocksInterface stocksService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("[Controller] 재고목록 가져오기 성공")
    void getAllStocksSuccess() throws Exception {
        //given
        given(stocksFacade.getAllStocks(any()))
                .willReturn(new ArrayList<StocksDto.StockResponse>(
                        Arrays.asList(
                                StocksDto.StockResponse.builder()
                                        .productId(1L)
                                        .productName("a")
                                        .price(1D)
                                        .amount(1D)
                                        .spec("b")
                                        .lackAmount(0D)
                                        .lackDate(null)
                                        .build()
                        )
                ));
        //when & then
        mockMvc.perform(get("/stocks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("a"))
                .andExpect(jsonPath("$[0].price").value(1D))
                .andExpect(jsonPath("$[0].amount").value(1D))
                .andExpect(jsonPath("$[0].spec").value("b"))
                .andExpect(jsonPath("$[0].lackAmount").value(0D));
    }
    @Test
    @WithMockUser
    @DisplayName("[Controller] 재고로트 가져오기 성공")
    void getLotByProductIdSuccess() throws Exception {
        //given
        given(stocksService.getLotByPid(anyLong()))
                .willReturn(new ArrayList<StocksDto.StockResponseLot>(
                        Arrays.asList(
                                StocksDto.StockResponseLot.builder()
                                        .productId(1L)
                                        .productName("a")
                                        .company("b")
                                        .price(1D)
                                        .amount(1D)
                                        .lot(LocalDate.of(2023, 6, 1))
                                        .build()
                        )
                ));

        //when & then
        mockMvc.perform(get("/stocks/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("a"))
                .andExpect(jsonPath("$[0].company").value("b"))
                .andExpect(jsonPath("$[0].price").value(1D))
                .andExpect(jsonPath("$[0].amount").value(1D))
                .andExpect(jsonPath("$[0].lot").value(
                        LocalDate.of(2023,6,1).toString()
                        )
                );
    }
}