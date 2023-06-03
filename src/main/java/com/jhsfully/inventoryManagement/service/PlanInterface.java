package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PlanDto;

import java.time.LocalDate;
import java.util.List;

public interface PlanInterface {

    public List<PlanDto.PlanResponse> getPlans(LocalDate startdate, LocalDate enddate);

    public PlanDto.PlanResponse addPlan(PlanDto.PlanAddRequest request);

    public PlanDto.PlanResponse updatePlan(PlanDto.PlanUpdateRequest request);

    public void deletePlan(Long id);

}
