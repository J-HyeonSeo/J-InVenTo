package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PlanDto;
import com.jhsfully.inventoryManagement.exception.PlanException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.entity.PlanEntity;
import com.jhsfully.inventoryManagement.entity.ProductEntity;
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
@Transactional
@AllArgsConstructor
public class PlanService implements PlanInterface{

    private final PlanRepository planRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
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
    private ProductEntity validateAddPlan(PlanDto.PlanAddRequest request){

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

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException(ProductErrorType.PRODUCT_NOT_FOUND));

        return product;
    }

    private PlanEntity validateAndUpdatePlan(PlanDto.PlanUpdateRequest request){

        boolean dueUpdate = false;
        boolean desUpdate = false;
        boolean amtUpdate = false;

        if(request.getDue() != null){
            if(request.getDue().isBefore(LocalDate.now())){
                throw new PlanException(PlanErrorType.PLAN_DUE_BEFORE_TODAY);
            }
            dueUpdate = true;
        }

        if(request.getDestination() != null){
            desUpdate = true;
        }

        if(request.getAmount() != null){
            if(request.getAmount() <= 0){
                throw new PlanException(PlanErrorType.PLAN_AMOUNT_HAS_NOT_OR_LESS_ZERO);
            }
            amtUpdate = true;
        }

        PlanEntity planEntity = planRepository.findById(request.getId())
                .orElseThrow(() -> new PlanException(PlanErrorType.PLAN_NOT_FOUND));

        if(dueUpdate)planEntity.setDue(request.getDue());
        if(desUpdate)planEntity.setDestination(request.getDestination());
        if(amtUpdate)planEntity.setAmount(request.getAmount());

        return planEntity;
    }
}
