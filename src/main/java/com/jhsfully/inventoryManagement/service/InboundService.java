package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.model.InboundEntity;
import com.jhsfully.inventoryManagement.repository.InboundRepository;
import com.jhsfully.inventoryManagement.repository.StocksRepository;
import com.jhsfully.inventoryManagement.type.StocksErrorType;
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
    private final StocksRepository stocksRepository;

    @Override
    public List<InboundDto.InboundResponse> getInbounds(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return inboundRepository.findInbounds(startDateTime, endDateTime);
    }

    @Override
    public InboundDto.InboundResponse addInbound(InboundDto.InboundAddRequest request) {
        //Stock에 대한 밸리데이션만 검증함. 나머진 파서드에서 처리.
        validateAddInbound(request);
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
        //outBound이력이 없는 출고에 한해서 삭제가 가능함 (일단 보류)
    }

    //============================== Validates ================================

    public void validateAddInbound(InboundDto.InboundAddRequest request){

        if(request.getStockId() == null || stocksRepository.existsById(request.getStockId())){
            throw new StocksException(StocksErrorType.STOCKS_NOT_FOUND);
        }

    }

}
