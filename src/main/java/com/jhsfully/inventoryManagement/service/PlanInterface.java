package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PlanDto;

import java.time.LocalDate;
import java.util.List;

public interface PlanInterface {

    List<PlanDto.PlanResponse> getPlans(LocalDate startdate, LocalDate enddate);

    PlanDto.PlanResponse addPlan(PlanDto.PlanAddRequest request);

    PlanDto.PlanResponse updatePlan(PlanDto.PlanUpdateRequest request);

    void deletePlan(Long id);

}
