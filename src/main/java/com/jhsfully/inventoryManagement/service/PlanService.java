package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PlanDto;
import com.jhsfully.inventoryManagement.exception.PlanException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.model.PlanEntity;
import com.jhsfully.inventoryManagement.repository.PlanRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.type.PlanErrorType;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PlanService implements PlanInterface{

    private final PlanRepository planRepository;
    private final ProductRepository productRepository;

    @Override
    public List<PlanDto.PlanResponse> getPlans(LocalDate startDate, LocalDate endDate) {

        if(endDate == null){
            return planRepository.findByDueGreaterThanEqualOrderByDueAsc(startDate)
                    .stream()
                    .map(PlanEntity::toDto)
                    .collect(Collectors.toList());
        }
        return planRepository.findByDueBetweenOrderByDueAsc(startDate, endDate)
                .stream()
                .map(PlanEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlanDto.PlanResponse addPlan(PlanDto.PlanAddRequest request) {
        validateAddPlan(request);
        PlanEntity planEntity = PlanEntity.builder()
                .productid(request.getProductId())
                .due(request.getDue())
                .destination(request.getDestination())
                .amount(request.getAmount())
                .build();
        return PlanEntity.toDto(planRepository.save(planEntity));
    }

    @Override
    public PlanDto.PlanResponse updatePlan(PlanDto.PlanUpdateRequest request) {
        PlanEntity updated = validateAndUpdatePlan(request);
        return PlanEntity.toDto(planRepository.save(updated));
    }

    @Override
    public void deletePlan(Long id) {
        if(!planRepository.existsById(id)){
            throw new PlanException(PlanErrorType.PLAN_NOT_FOUND);
        }
        planRepository.deleteById(id);
    }

    //======================== Validates ===========================
    public void validateAddPlan(PlanDto.PlanAddRequest request){

        if(request.getProductId() == null){
            throw new ProductException(ProductErrorType.PRODUCT_ID_NULL);
        }

        if(!productRepository.existsById(request.getProductId())){
            throw new ProductException(ProductErrorType.PRODUCT_NOT_FOUND);
        }

        if(request.getDue() == null){
            throw new PlanException(PlanErrorType.PLAN_DUE_IS_NULL);
        }

        if(request.getDue().isBefore(LocalDate.now())){
            throw new PlanException(PlanErrorType.PLAN_DUE_BEFORE_TODAY);
        }

        if(request.getAmount() == null){
            throw new PlanException(PlanErrorType.PLAN_AMOUNT_IS_NULL);
        }

        if(request.getAmount() <= 0){
            throw new PlanException(PlanErrorType.PLAN_AMOUNT_HAS_NOT_OR_LESS_ZERO);
        }

    }

    public PlanEntity validateAndUpdatePlan(PlanDto.PlanUpdateRequest request){

        PlanEntity planEntity = planRepository.findById(request.getId())
                .orElseThrow(() -> new PlanException(PlanErrorType.PLAN_NOT_FOUND));

        if(request.getDue() != null){

            if(request.getDue().isBefore(LocalDate.now())){
                throw new PlanException(PlanErrorType.PLAN_DUE_BEFORE_TODAY);
            }

            planEntity.setDue(request.getDue());

        }

        if(request.getDestination() != null){
            planEntity.setDestination(request.getDestination());
        }

        if(request.getAmount() != null){
            if(request.getAmount() <= 0){
                throw new PlanException(PlanErrorType.PLAN_AMOUNT_HAS_NOT_OR_LESS_ZERO);
            }
            planEntity.setAmount(request.getAmount());
        }

        return planEntity;
    }
}
