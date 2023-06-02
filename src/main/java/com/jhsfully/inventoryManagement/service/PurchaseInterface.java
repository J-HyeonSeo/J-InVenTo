package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseInterface {

    public List<PurchaseDto.PurchaseResponse> getPurchases(LocalDate startDate, LocalDate endDate);

    public PurchaseDto.PurchaseResponse addPurchase(PurchaseDto.PurchaseAddRequest request);

    public void deletePurchase(Long id);

}
