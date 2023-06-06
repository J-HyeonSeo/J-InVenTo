package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.exception.PurchaseException;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.model.PurchaseEntity;
import com.jhsfully.inventoryManagement.repository.BomRepository;
import com.jhsfully.inventoryManagement.repository.InboundRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.repository.PurchaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.PurchaseErrorType.*;

@Service
@AllArgsConstructor
public class PurchaseService implements PurchaseInterface{
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final InboundRepository inboundRepository;
    private final BomRepository bomRepository;

    @Override
    public PurchaseDto.PurchaseResponse getPurchase(Long id){
        return PurchaseEntity.toDto(purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseException(PURCHASE_NOT_FOUND)));
    }

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

        ProductEntity product = validateAddPurchase(request);

        PurchaseEntity purchaseEntity = PurchaseEntity.builder()
                .product(product)
                .at(LocalDateTime.now())
                .amount(request.getAmount())
                .price(request.getPrice())
                .company(request.getCompany())
                .note(request.getNote())
                .build();

        return PurchaseEntity.toDto(purchaseRepository.save(purchaseEntity));
    }

    @Override
    public void deletePurchase(Long id) {
        //입고단에서 해당 ID를 참조하지 않을 경우에만 삭제 가능!!
        PurchaseEntity purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseException(PURCHASE_NOT_FOUND));

        if(inboundRepository.existsByPurchase(purchase)){
            throw new PurchaseException(PURCHASE_HAS_INBOUND);
        }

        purchaseRepository.delete(purchase);
    }


    //============================ Validates ==================================
    public ProductEntity validateAddPurchase(PurchaseDto.PurchaseAddRequest request){

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
        //bom의 부모에 존재하면 안됨.
        if(bomRepository.existsByParentProduct(product)){
            throw new PurchaseException(PURCHASE_CANT_PARENT_PRODUCT);
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

        return product;
    }

}
