package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.exception.OutboundException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.model.OutboundDetailsEntity;
import com.jhsfully.inventoryManagement.model.OutboundEntity;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.model.StocksEntity;
import com.jhsfully.inventoryManagement.repository.OutboundDetailRepository;
import com.jhsfully.inventoryManagement.repository.OutboundRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.repository.StocksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.OutboundErrorType.*;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_NOT_FOUND;

@Service
@AllArgsConstructor
public class OutboundService implements OutboundInterface{

    private final OutboundRepository outboundRepository;
    private final OutboundDetailRepository outboundDetailRepository;
    private final ProductRepository productRepository;
    private final StocksRepository stocksRepository;

    @Override
    public Long countByStock(Long stockId){
        StocksEntity stocksEntity = stocksRepository.findById(stockId)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));
        return outboundDetailRepository.countByStock(stocksEntity);
    }

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
        return outboundDetailRepository.findByOutbound(outbound)
                .stream()
                .map(OutboundDetailsEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OutboundDto.OutboundResponse addOutbound(OutboundDto.OutboundAddRequest request) {

        validateAddOutbound(request);

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        OutboundEntity outbound = OutboundEntity.builder()
                .product(product)
                .destination(request.getDestination())
                .amount(request.getAmount())
                .at(LocalDateTime.now())
                .note(request.getNote())
                .build();

        return OutboundEntity.toDto(outboundRepository.save(outbound));
    }

    @Override
    @Transactional
    public void addOutboundDetail(OutboundDto.OutboundDetailAddRequest request) {

        validateAddOutboundDetail(request);

        OutboundEntity outbound = outboundRepository.findById(request.getOutboundId())
                .orElseThrow(() -> new OutboundException(OUTBOUND_NOT_FOUND));

        StocksEntity stock = stocksRepository.findById(request.getStockId())
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));

        OutboundDetailsEntity outboundDetailsEntity = OutboundDetailsEntity
                .builder()
                .outbound(outbound)
                .stock(stock)
                .amount(request.getAmount())
                .build();

        outboundDetailRepository.save(outboundDetailsEntity);
    }

    @Override
    @Transactional
    public void deleteOutbound(Long outboundId) {
        OutboundEntity outbound = outboundRepository.findById(outboundId)
                .orElseThrow(() -> new OutboundException(OUTBOUND_NOT_FOUND));

        outboundRepository.delete(outbound);
    }

    @Override
    @Transactional
    public OutboundDto.OutboundDetailResponse deleteOutboundDetail(Long detailId) {
        OutboundDetailsEntity outboundDetail = outboundDetailRepository.findById(detailId)
                .orElseThrow(() -> new OutboundException(OUTBOUND_DETAILS_NOT_FOUND));

        OutboundDto.OutboundDetailResponse response = OutboundDetailsEntity.toDto(outboundDetail);

        outboundDetailRepository.delete(outboundDetail);

        return response;
    }

    //======================== Validates===================================

    public void validateAddOutbound(OutboundDto.OutboundAddRequest request){

        if(request.getAmount() == null){
            throw new OutboundException(OUTBOUND_AMOUNT_NULL);
        }

        if(request.getAmount() <= 0){
            throw new OutboundException(OUTBOUND_AMOUNT_OR_LESS_ZERO);
        }

    }

    public void validateAddOutboundDetail(OutboundDto.OutboundDetailAddRequest request){

        if(request.getAmount() == null){
            throw new OutboundException(OUTBOUND_AMOUNT_NULL);
        }

        if(request.getAmount() <= 0){
            throw new OutboundException(OUTBOUND_AMOUNT_OR_LESS_ZERO);
        }

    }
}
