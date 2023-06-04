package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.repository.InboundRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class InboundService implements InboundInterface{

    private final InboundRepository inboundRepository;

    @Override
    public List<InboundDto.InboundResponse> getInbounds(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return inboundRepository.findInbounds(startDateTime, endDateTime);
    }

    @Override
    public InboundDto.InboundResponse addInbound(InboundDto.InboundAddRequest request) {
        return null;
    }

    @Override
    public void deleteInbound(Long id) {
        //outBound이력이 없는 출고에 한해서 삭제가 가능함 (일단 보류)
    }

    //============================== Validates ================================

    public void validateAddInbound(InboundDto.InboundAddRequest request){

    }

}
