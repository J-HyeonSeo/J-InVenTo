package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PlanDto;
import com.jhsfully.inventoryManagement.entity.PlanEntity;
import com.jhsfully.inventoryManagement.exception.PlanException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.repository.PlanRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static com.jhsfully.inventoryManagement.type.PlanErrorType.*;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PlanServiceTest {

    @BeforeAll
    static void setup(@Autowired DataSource dataSource){
        try(Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/product.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/testdatas/plan.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanRepository planRepository;

    @Test
    @DisplayName("[Service]플랜 가져오기 성공")
    void getPlansSuccess(){
        //when
        List<PlanDto.PlanResponse> plans = planService.getPlans(LocalDate.of(2023, 6, 1),
                LocalDate.of(2023, 6, 20));

        //then
        assertEquals(2, plans.size());
    }

    @Test
    @DisplayName("[Service]플랜 추가 성공")
    @Transactional
    void addPlanSuccess(){
        //given
        PlanDto.PlanAddRequest request = PlanDto.PlanAddRequest.builder()
                .productId(1L)
                .amount(100D)
                .destination("fff")
                .due(LocalDate.of(2099, 12, 12))
                .build();

        //when
        Long planID = planService.addPlan(request).getId();

        //then
        PlanEntity planEntity = planRepository.findById(planID).orElseThrow();

        assertAll(
                () -> assertEquals(request.getProductId(), planEntity.getProduct().getId()),
                () -> assertEquals(request.getAmount(), planEntity.getAmount()),
                () -> assertEquals(request.getDestination(), planEntity.getDestination()),
                () -> assertEquals(request.getDue(), planEntity.getDue())
        );
    }

    @Test
    @DisplayName("[Service]플랜 수정 성공")
    @Transactional
    void updatePlanSuccess(){
        //given
        PlanDto.PlanUpdateRequest request = PlanDto.PlanUpdateRequest.builder()
                .id(1L)
                .amount(100D)
                .destination("fff")
                .due(LocalDate.of(2099, 12, 12))
                .build();

        //when
        planService.updatePlan(request);

        //then
        PlanEntity planEntity = planRepository.findById(1L).orElseThrow();

        assertAll(
                () -> assertEquals(request.getId(), planEntity.getId()),
                () -> assertEquals(request.getAmount(), planEntity.getAmount()),
                () -> assertEquals(request.getDestination(), planEntity.getDestination()),
                () -> assertEquals(request.getDue(), planEntity.getDue())
        );
    }

    @Test
    @DisplayName("[Service]플랜 삭제 성공")
    @Transactional
    void deletePlanSuccess(){

        //when
        planService.deletePlan(1L);

        PlanException exception = assertThrows(PlanException.class,
                () -> planService.deletePlan(1L));

        //then
        assertEquals(PLAN_NOT_FOUND, exception.getPlanErrorType());

    }

    //======================== Fail Cases ======================================

    //add plan fail
    @Test
    @DisplayName("[Service]플랜 추가 실패 - 플랜 기한 NULL")
    void addPlanPlanDueIsNullFail(){
        //given
        PlanDto.PlanAddRequest request = PlanDto.PlanAddRequest.builder()
                .productId(1L)
                .amount(100D)
                .destination("fff")
                .due(null)
                .build();

        //when
        PlanException exception = assertThrows(PlanException.class,
                () -> planService.addPlan(request));

        //then
        assertEquals(PLAN_DUE_IS_NULL, exception.getPlanErrorType());
    }

    @Test
    @DisplayName("[Service]플랜 추가 실패 - 플랜 기한 오늘 이전")
    void addPlanPlanDueBeforeTodayFail(){
        //given
        PlanDto.PlanAddRequest request = PlanDto.PlanAddRequest.builder()
                .productId(1L)
                .amount(100D)
                .destination("fff")
                .due(LocalDate.of(2023, 6, 12))
                .build();

        //when
        PlanException exception = assertThrows(PlanException.class,
                () -> planService.addPlan(request));

        //then
        assertEquals(PLAN_DUE_BEFORE_TODAY, exception.getPlanErrorType());
    }

    @Test
    @DisplayName("[Service]플랜 추가 실패 - 플랜 수량 NULL")
    void addPlanPlanAmountIsNullFail(){
        //given
        PlanDto.PlanAddRequest request = PlanDto.PlanAddRequest.builder()
                .productId(1L)
                .amount(null)
                .destination("fff")
                .due(LocalDate.of(2099, 6, 17))
                .build();

        //when
        PlanException exception = assertThrows(PlanException.class,
                () -> planService.addPlan(request));

        //then
        assertEquals(PLAN_AMOUNT_IS_NULL, exception.getPlanErrorType());
    }

    @Test
    @DisplayName("[Service]플랜 추가 실패 - 플랜 수량 0이하")
    void addPlanPlanAmountOrLessZeroFail(){
        //given
        PlanDto.PlanAddRequest request = PlanDto.PlanAddRequest.builder()
                .productId(1L)
                .amount(0D)
                .destination("fff")
                .due(LocalDate.of(2099, 6, 17))
                .build();

        //when
        PlanException exception = assertThrows(PlanException.class,
                () -> planService.addPlan(request));

        //then
        assertEquals(PLAN_AMOUNT_HAS_NOT_OR_LESS_ZERO, exception.getPlanErrorType());
    }

    @Test
    @DisplayName("[Service]플랜 추가 실패 - 품목 X")
    void addPlanProductNotFoundFail(){
        //given
        PlanDto.PlanAddRequest request = PlanDto.PlanAddRequest.builder()
                .productId(10000L)
                .amount(1000D)
                .destination("fff")
                .due(LocalDate.of(2099, 6, 17))
                .build();

        //when
        ProductException exception = assertThrows(ProductException.class,
                () -> planService.addPlan(request));

        //then
        assertEquals(PRODUCT_NOT_FOUND, exception.getProductErrorType());
    }



    //update plan fail

    @Test
    @DisplayName("[Service]플랜 수정 실패 - 플랜 기한 오늘 이전")
    void updatePlanPlanDueBeforeTodayFail(){
        //given
        PlanDto.PlanUpdateRequest request = PlanDto.PlanUpdateRequest.builder()
                .id(1L)
                .amount(100D)
                .destination("fff")
                .due(LocalDate.of(2023, 6, 12))
                .build();

        //when
        PlanException exception = assertThrows(PlanException.class,
                () -> planService.updatePlan(request));

        //then
        assertEquals(PLAN_DUE_BEFORE_TODAY, exception.getPlanErrorType());
    }

    @Test
    @DisplayName("[Service]플랜 수정 실패 - 플랜 수량 0이하")
    void updatePlanPlanAmountOrLessZeroFail(){
        //given
        PlanDto.PlanUpdateRequest request = PlanDto.PlanUpdateRequest.builder()
                .id(1L)
                .amount(0D)
                .destination("fff")
                .due(LocalDate.of(2099, 6, 17))
                .build();

        //when
        PlanException exception = assertThrows(PlanException.class,
                () -> planService.updatePlan(request));

        //then
        assertEquals(PLAN_AMOUNT_HAS_NOT_OR_LESS_ZERO, exception.getPlanErrorType());
    }

    @Test
    @DisplayName("[Service]플랜 수정 실패 - 플랜 X")
    void updatePlanPlanNotFoundFail(){
        //given
        PlanDto.PlanUpdateRequest request = PlanDto.PlanUpdateRequest.builder()
                .id(10000L)
                .amount(100D)
                .destination("fff")
                .due(LocalDate.of(2099, 6, 17))
                .build();

        //when
        PlanException exception = assertThrows(PlanException.class,
                () -> planService.updatePlan(request));

        //then
        assertEquals(PLAN_NOT_FOUND, exception.getPlanErrorType());
    }

    //delete plan fail
    @Test
    @DisplayName("[Service]플랜 삭제 실패 - 플랜 X")
    void deletePlanPlanNotFoundFail(){
        //when
        PlanException exception = assertThrows(PlanException.class,
                () -> planService.deletePlan(10000L));
        //then
        assertEquals(PLAN_NOT_FOUND, exception.getPlanErrorType());
    }

}