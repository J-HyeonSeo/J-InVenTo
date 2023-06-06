package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.OutboundException;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.model.StocksEntity;
import com.jhsfully.inventoryManagement.service.*;
import com.jhsfully.inventoryManagement.type.OutboundErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jhsfully.inventoryManagement.type.OutboundErrorType.*;


//Controller => Facade => Services => Repositories
@Component
@AllArgsConstructor
public class OutboundFacade {

    private final OutboundInterface outboundService;
    private final BomInterface bomService;
    private final StocksInterface stocksService;
    private final ProductInterface productService;

    public List<OutboundDto.OutboundResponse> getOutbounds(LocalDate startDate, LocalDate endDate){
        return outboundService.getOutbounds(startDate, endDate);
    }

    public List<OutboundDto.OutboundDetailResponse> getOutboundDetails(Long outboundId){
        return outboundService.getOutboundDetails(outboundId);
    }

    public void executeOutbound(OutboundDto.OutboundAddRequest request){

        validateExecute(request); //밸리데이션 수행

        //출고 이력 발행
        OutboundDto.OutboundResponse outboundResponse = outboundService
                .addOutbound(request);

        //출고 상세 이력 발행 및 stock차감 수행.
        for(var stock : request.getStocks()){
            outboundService.addOutboundDetail(OutboundDto.OutboundDetailAddRequest
                            .builder()
                            .outboundId(outboundResponse.getId())
                            .stockId(stock.getStockId())
                            .amount(stock.getAmount())
                            .build());
            stocksService.spendStockById(stock.getStockId(), stock.getAmount());
        }

    }

    //=========================== validates ==================================

    private void validateExecute(OutboundDto.OutboundAddRequest request){
        if(request.getAmount() <= 0){
            throw new OutboundException(OUTBOUND_AMOUNT_OR_LESS_ZERO);
        }

        HashMap<Long, Double> requestStockSum = new HashMap<>(); //요청 Stock 합계
        HashMap<Long, Double> realStockSum = new HashMap<>(); //실제 Stock 합계
        HashMap<Long, Double> productStockSum = new HashMap<>(); //품목별 Stock 합계

        for(var stockRequest : request.getStocks()){
            if(stockRequest.getAmount() <= 0){
                throw new OutboundException(OUTBOUND_AMOUNT_OR_LESS_ZERO);
            }

            StocksDto.StockResponseLot stockResponse = stocksService.getStock(stockRequest.getStockId());
            ProductDto.ProductResponse productResponse = productService.getProduct(stockResponse.getProductId());

            Double requestSum = requestStockSum.getOrDefault(stockResponse.getId(), 0D);
            requestStockSum.put(stockResponse.getId(), requestSum + stockRequest.getAmount());

            Double realSum = realStockSum.getOrDefault(stockResponse.getId(), 0D);
            realStockSum.put(stockResponse.getId(), realSum + stockResponse.getAmount());

            Double productSum = productStockSum.getOrDefault(stockResponse.getId(), 0D);
            productStockSum.put(productResponse.getId(), productSum + stockResponse.getAmount());
        }

        //요청 수량이 재고 수량을 넘는지 검증함.
        for(Map.Entry<Long, Double> stock : requestStockSum.entrySet()){
            Double requests = stock.getValue();
            Double reals = realStockSum.get(stock.getKey());

            if(requests > reals){
                throw new OutboundException(OUTBOUND_EXCEED_STOCKS);
            }
        }

        //BOM LEAF 기준으로 필요수량 산출하기.
        List<BomDto.BomLeaf> productLeaves = bomService.getLeafProducts(request.getProductId());

        //요청된 수량이, BOM에 정의된 수량만큼 정확히 요청되었는지 검증함.
        for(var product : productLeaves){

            Double requiredAmount = product.getCost() * request.getAmount();

            Double requestAmount = productStockSum.getOrDefault(product.getProductId(), 0D);

            if(requestAmount != requiredAmount){
                throw new OutboundException(OUTBOUND_EXCEED_REQUIRES);
            }

        }
    }

}
