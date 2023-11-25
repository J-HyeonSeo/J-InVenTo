package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.InboundDto;

import java.time.LocalDate;
import java.util.List;

public interface InboundInterface {

    Double getInboundsByPurchase(Long purchaseId);

    InboundDto.InboundResponse getInbound(Long id);

    List<InboundDto.InboundResponse> getInbounds(LocalDate startDate, LocalDate endDate);

    InboundDto.InboundResponse addInbound(InboundDto.InboundAddRequest request);

    void deleteInbound(Long id);

}
