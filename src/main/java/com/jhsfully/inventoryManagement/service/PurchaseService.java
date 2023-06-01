package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.exception.PurchaseException;
import com.jhsfully.inventoryManagement.model.PurchaseEntity;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.repository.PurchaseRepository;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.ProductErrorType.*;
import static com.jhsfully.inventoryManagement.type.PurchaseErrorType.*;

@Service
@AllArgsConstructor
public class PurchaseService implements PurchaseInterface{
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;

    @Override
    public List<PurchaseDto.PurchaseResponse> getPurchases(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return purchaseRepository.findByAtBetween(startDateTime, endDateTime)
                .stream().map(PurchaseEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseDto.PurchaseResponse addPurchase(PurchaseDto.PurchaseAddRequest request) {

        validateAddPurchase(request);

        PurchaseEntity purchaseEntity = PurchaseEntity.builder()
                .productid(request.getProductId())
                .at(LocalDateTime.now())
                .amount(request.getAmount())
                .price(request.getPrice())
                .note(request.getNote())
                .build();

        return PurchaseEntity.toDto(purchaseRepository.save(purchaseEntity));
    }

    @Override
    public void updatePurchase() {
        //구현 보류, 입고단에서 해당 ID를 참조하지 않을 경우에만 삭제 가능!!
    }

    @Override
    public void deletePurchase(Long id) {
        //구현 보류, 입고단에서 해당 ID를 참조하지 않을 경우에만 삭제 가능!!
    }

    //============================ Validates ==================================
    public void validateAddPurchase(PurchaseDto.PurchaseAddRequest request){
        if(request.getProductId() == null){
            throw new ProductException(PRODUCT_ID_NULL);
        }
        //품목 ID가 존재해야함.
        if(!productRepository.existsById(request.getProductId())){
            throw new ProductException(PRODUCT_NOT_FOUND);
        }
        //amount가 존재해야함.
        if(request.getAmount() == null){
            throw new PurchaseException(PURCHASE_AMOUNT_NULL);
        }
        //amount가 0보다 커야함.
        if(request.getAmount() <= 0){
            throw new PurchaseException(PURCHASE_AMOUNT_LESS_ZERO);
        }
        //price가 존재해야함.
        if(request.getPrice() == null){
            throw new PurchaseException(PURCHASE_PRICE_NULL);
        }
        //price가 0보다 커야함.
        if(request.getPrice() <= 0){
            throw new PurchaseException(PURCHASE_AMOUNT_LESS_ZERO);
        }
    }

}
