package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.model.InboundEntity;
import com.jhsfully.inventoryManagement.repository.InboundRepository;
import com.jhsfully.inventoryManagement.type.InboundErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InboundService implements InboundInterface{

    private final InboundRepository inboundRepository;

    public List<InboundDto.InboundResponse> getInboundsByPurchase(Long purchaseId){
        return inboundRepository.findByPurchaseid(purchaseId)
                .stream()
                .map(InboundEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InboundDto.InboundResponse> getInbounds(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return inboundRepository.findInbounds(startDateTime, endDateTime);
    }


    @Override
    public InboundDto.InboundResponse addInbound(InboundDto.InboundAddRequest request) {
        //Stock을 추가할 때, 이미 밸리데이션 검증이 완료됨.
        InboundEntity entity = InboundEntity.builder()
                .purchaseid(request.getPurchaseId())
                .stockid(request.getStockId())
                .at(LocalDateTime.now())
                .amount(request.getAmount())
                .company(request.getCompany())
                .note(request.getNote())
                .build();
        return InboundEntity.toDto(inboundRepository.save(entity));
    }


    @Override
    public void deleteInbound(Long id) {
        //파서드 => 밸리데이션 => 서비스 => 삭제(즉, 그냥 삭제만 하면 됨)
        InboundEntity inboundEntity = inboundRepository.findById(id)
                .orElseThrow(() -> new InboundException(InboundErrorType.INBOUND_NOT_FOUND));

        inboundRepository.delete(inboundEntity);
    }

}
