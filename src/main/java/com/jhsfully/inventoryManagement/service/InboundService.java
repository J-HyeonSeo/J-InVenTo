package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.exception.PurchaseException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.model.InboundEntity;
import com.jhsfully.inventoryManagement.model.PurchaseEntity;
import com.jhsfully.inventoryManagement.model.StocksEntity;
import com.jhsfully.inventoryManagement.repository.InboundRepository;
import com.jhsfully.inventoryManagement.repository.PurchaseRepository;
import com.jhsfully.inventoryManagement.repository.StocksRepository;
import com.jhsfully.inventoryManagement.type.InboundErrorType;
import com.jhsfully.inventoryManagement.type.PurchaseErrorType;
import com.jhsfully.inventoryManagement.type.StocksErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.InboundErrorType.*;

@Service
@AllArgsConstructor
public class InboundService implements InboundInterface{

    private final InboundRepository inboundRepository;
    private final PurchaseRepository purchaseRepository;
    private final StocksRepository stocksRepository;

    public List<InboundDto.InboundResponse> getInboundsByPurchase(Long purchaseId){

        PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new PurchaseException(PurchaseErrorType.PURCHASE_NOT_FOUND));

        return inboundRepository.findByPurchase(purchase)
                .stream()
                .map(InboundEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public InboundDto.InboundResponse getInbound(Long id){
        return InboundEntity.toDto(inboundRepository.findById(id)
                .orElseThrow(() -> new InboundException(INBOUND_NOT_FOUND)));
    }

    @Override
    public List<InboundDto.InboundResponse> getInbounds(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return inboundRepository.findByAtBetween(startDateTime, endDateTime)
                .stream()
                .map(InboundEntity::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public InboundDto.InboundResponse addInbound(InboundDto.InboundAddRequest request) {
        //Stock을 추가할 때, 이미 밸리데이션 검증이 완료됨.

        PurchaseEntity purchase = purchaseRepository.findById(request.getPurchaseId())
                .orElseThrow(() -> new PurchaseException(PurchaseErrorType.PURCHASE_NOT_FOUND));

        StocksEntity stock = stocksRepository.findById(request.getStockId())
                .orElseThrow(() -> new StocksException(StocksErrorType.STOCKS_NOT_FOUND));

        InboundEntity entity = InboundEntity.builder()
                .purchase(purchase)
                .stock(stock)
                .at(LocalDateTime.now())
                .amount(request.getAmount())
                .note(request.getNote())
                .build();
        return InboundEntity.toDto(inboundRepository.save(entity));
    }


    @Override
    @Transactional
    public void deleteInbound(Long id) {
        //파서드 => 밸리데이션 => 서비스 => 삭제(즉, 그냥 삭제만 하면 됨)
        InboundEntity inboundEntity = inboundRepository.findById(id)
                .orElseThrow(() -> new InboundException(INBOUND_NOT_FOUND));

        inboundRepository.delete(inboundEntity);
    }

}
