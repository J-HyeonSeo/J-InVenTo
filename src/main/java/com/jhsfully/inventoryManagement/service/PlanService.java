package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PlanDto;
import com.jhsfully.inventoryManagement.exception.PlanException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.model.PlanEntity;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.repository.PlanRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.type.PlanErrorType;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public PlanDto.PlanResponse addPlan(PlanDto.PlanAddRequest request) {

        ProductEntity product = validateAddPlan(request);

        PlanEntity planEntity = PlanEntity.builder()
                .product(product)
                .due(request.getDue())
                .destination(request.getDestination())
                .amount(request.getAmount())
                .build();
        return PlanEntity.toDto(planRepository.save(planEntity));
    }

    @Override
    @Transactional
    public PlanDto.PlanResponse updatePlan(PlanDto.PlanUpdateRequest request) {
        PlanEntity updated = validateAndUpdatePlan(request);
        return PlanEntity.toDto(planRepository.save(updated));
    }

    @Override
    @Transactional
    public void deletePlan(Long id) {
        if(!planRepository.existsById(id)){
            throw new PlanException(PlanErrorType.PLAN_NOT_FOUND);
        }
        planRepository.deleteById(id);
    }

    //======================== Validates ===========================
    public ProductEntity validateAddPlan(PlanDto.PlanAddRequest request){

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException(ProductErrorType.PRODUCT_NOT_FOUND));

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

        return product;
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
