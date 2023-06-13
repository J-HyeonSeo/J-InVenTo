package com.jhsfully.inventoryManagement.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhsfully.inventoryManagement.dto.PlanDto;
import com.jhsfully.inventoryManagement.security.JwtAuthenticationFilter;
import com.jhsfully.inventoryManagement.security.SecurityConfiguration;
import com.jhsfully.inventoryManagement.service.PlanService;
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

import java.time.LocalDate;
import java.util.ArrayList;
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

@WebMvcTest(value = PlanController.class, excludeFilters =
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfiguration.class, JwtAuthenticationFilter.class}))
class PlanControllerTest {

    @MockBean
    private PlanService planService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[Controller]플랜 가져오기 성공")
    @WithMockUser()
    void getPlansSuccess() throws Exception {
        //given
        given(planService.getPlans(any(), any()))
                .willReturn(new ArrayList<PlanDto.PlanResponse>(
                        Arrays.asList(
                                PlanDto.PlanResponse.builder()
                                        .id(1L)
                                        .productId(1L)
                                        .productName("a")
                                        .amount(1D)
                                        .destination("b")
                                        .due(LocalDate.now())
                                        .build()
                        )
                ));

        //when & then
        mockMvc.perform(get("/plan?startDate=20230101&endDate=20230101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("a"))
                .andExpect(jsonPath("$[0].amount").value(1D))
                .andExpect(jsonPath("$[0].destination").value("b"))
                .andExpect(jsonPath("$[0].due").value(LocalDate.now().toString()));
    }

    @Test
    @DisplayName("[Controller]플랜 추가하기 성공")
    @WithMockUser()
    void addPlanSuccess() throws Exception {
        //given
        given(planService.addPlan(any()))
                .willReturn(PlanDto.PlanResponse.builder()
                        .id(1L)
                        .productId(1L)
                        .productName("a")
                        .amount(1D)
                        .destination("b")
                        .due(LocalDate.now())
                        .build());

        PlanDto.PlanAddRequest request = PlanDto.PlanAddRequest.builder().build();

        //when & then
        mockMvc.perform(post("/plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.productName").value("a"))
                .andExpect(jsonPath("$.amount").value(1D))
                .andExpect(jsonPath("$.destination").value("b"))
                .andExpect(jsonPath("$.due").value(LocalDate.now().toString()));
    }

    @Test
    @WithMockUser()
    @DisplayName("[Controller]플랜 수정 성공")
    void updatePlanSuccess() throws Exception {
        //given
        given(planService.updatePlan(any()))
                .willReturn(PlanDto.PlanResponse.builder()
                        .id(1L)
                        .productId(1L)
                        .productName("a")
                        .amount(1D)
                        .destination("b")
                        .due(LocalDate.now())
                        .build());

        PlanDto.PlanAddRequest request = PlanDto.PlanAddRequest.builder().build();

        //when & then
        mockMvc.perform(put("/plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.productName").value("a"))
                .andExpect(jsonPath("$.amount").value(1D))
                .andExpect(jsonPath("$.destination").value("b"))
                .andExpect(jsonPath("$.due").value(LocalDate.now().toString()));
    }

    @Test
    @WithMockUser()
    @DisplayName("[Controller]플랜 삭제 성공")
    void deletePlanSuccess() throws Exception {

        //when & then
        mockMvc.perform(delete("/plan/123")
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("123"));

        verify(planService, times(1)).deletePlan(anyLong());
    }

}