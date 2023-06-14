package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.OutboundDto;

import java.time.LocalDate;
import java.util.List;

public interface OutboundInterface {
    public Double countByStock(Long stockId);

    public List<OutboundDto.OutboundResponse> getOutbounds(LocalDate startDate,
                                                           LocalDate endDate);

    public List<OutboundDto.OutboundDetailResponse> getOutboundDetails(Long outboundId);

    public OutboundDto.OutboundResponse addOutbound(OutboundDto.OutboundAddRequest request);

    public void addOutboundDetail(OutboundDto.OutboundDetailAddRequest request);

    public void deleteOutbound(Long outboundId);

    public OutboundDto.OutboundDetailResponse deleteOutboundDetail(Long detailId);

}
