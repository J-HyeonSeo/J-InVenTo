package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.exception.OutboundException;
import com.jhsfully.inventoryManagement.model.OutboundEntity;
import com.jhsfully.inventoryManagement.repository.OutboundDetailRepository;
import com.jhsfully.inventoryManagement.repository.OutboundRepository;
import com.jhsfully.inventoryManagement.type.OutboundErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.OutboundErrorType.*;

@Service
@AllArgsConstructor
public class OutboundService implements OutboundInterface{

    private final OutboundRepository outboundRepository;
    private final OutboundDetailRepository outboundDetailRepository;

    @Override
    public List<OutboundDto.OutboundResponse> getOutbounds(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return outboundRepository.findByAtBetween(startDateTime, endDateTime)
                .stream()
                .map(OutboundEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutboundDto.OutboundDetailResponse> getOutboundDetails(Long outboundId) {
        OutboundEntity outbound = outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundException(OUTBOUND_NOT_FOUND));
        return outboundDetailRepository.findByOutbound(outbound);
    }

    @Override
    public void addOutbound(OutboundDto.OutboundAddRequest request) {

    }

    @Override
    public void addOutboundDetail(OutboundDto.OutboundDetailAddRequest request) {

    }

    @Override
    public void deleteOutbound(Long outboundId) {

    }

    @Override
    public void deleteOutboundDetail(Long detailId) {

    }
}
