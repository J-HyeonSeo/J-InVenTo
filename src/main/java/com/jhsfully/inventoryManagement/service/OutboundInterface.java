package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.OutboundDto;

import java.time.LocalDate;
import java.util.List;

public interface OutboundInterface {

    public List<OutboundDto.OutboundResponse> getOutbounds(LocalDate startDate,
                                                           LocalDate endDate);

    public List<OutboundDto.OutboundDetailResponse> getOutboundDetails(Long outboundId);

    public void addOutbound(OutboundDto.OutboundAddRequest request);

    public void addOutboundDetail(OutboundDto.OutboundDetailAddRequest request);

    public void deleteOutbound(Long outboundId);

    public void deleteOutboundDetail(Long detailId);

}
