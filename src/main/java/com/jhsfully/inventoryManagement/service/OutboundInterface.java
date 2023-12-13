package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.OutboundDto;

import java.time.LocalDate;
import java.util.List;

public interface OutboundInterface {
    Double countByStock(Long stockId);

    List<OutboundDto.OutboundResponse> getOutbounds(LocalDate startDate,
                                                           LocalDate endDate);

    List<OutboundDto.OutboundDetailResponse> getOutboundDetails(Long outboundId);

    OutboundDto.OutboundResponse addOutbound(OutboundDto.OutboundAddRequest request);

    void addOutboundDetail(OutboundDto.OutboundDetailAddRequest request);

    void deleteOutbound(Long outboundId);

    OutboundDto.OutboundDetailResponse deleteOutboundDetail(Long detailId);

}
