package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseInterface {

    PurchaseDto.PurchaseResponse getPurchase(Long id);
    List<PurchaseDto.PurchaseResponse> getPurchases(LocalDate startDate, LocalDate endDate);

    PurchaseDto.PurchaseResponse addPurchase(PurchaseDto.PurchaseAddRequest request);

    void deletePurchase(Long id);

}
