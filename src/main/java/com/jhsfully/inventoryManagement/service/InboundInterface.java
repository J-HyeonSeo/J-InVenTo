package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.InboundDto;

import java.time.LocalDate;
import java.util.List;

public interface InboundInterface {

    public List<InboundDto.InboundResponse> getInboundsByPurchase(Long purchaseId);

    public InboundDto.InboundResponse getInbound(Long id);

    public List<InboundDto.InboundResponse> getInbounds(LocalDate startDate, LocalDate endDate);

    public InboundDto.InboundResponse addInbound(InboundDto.InboundAddRequest request);

    public void deleteInbound(Long id);

}
