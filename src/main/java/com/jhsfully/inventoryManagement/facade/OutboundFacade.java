package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.OutboundException;
import com.jhsfully.inventoryManagement.service.BomInterface;
import com.jhsfully.inventoryManagement.service.OutboundInterface;
import com.jhsfully.inventoryManagement.service.ProductInterface;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Long executeOutbound(OutboundDto.OutboundAddRequest request){

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
        return outboundResponse.getId();
    }


    //출고 전체 단위로 취소
    @Transactional
    public void cancelOutbound(Long outboundId){
        List<OutboundDto.OutboundDetailResponse> outboundDetails = outboundService
                .getOutboundDetails(outboundId);
        for(var outboundDetail : outboundDetails){
            cancelOutboundDetail(outboundDetail.getId());
        }
        outboundService.deleteOutbound(outboundId);
    }

    //출고 상세 단위로 취소
    @Transactional
    public void cancelOutboundDetail(Long detailId){
        OutboundDto.OutboundDetailResponse detailResponse = outboundService.deleteOutboundDetail(detailId);
        stocksService.cancelSpendStockById(detailResponse.getStockId(), detailResponse.getAmount());
    }

    //=========================== validates ==================================

    @Transactional
    private void validateExecute(OutboundDto.OutboundAddRequest request){
        if(request.getAmount() == null){
            throw new OutboundException(OUTBOUND_AMOUNT_NULL);
        }

        if(request.getAmount() <= 0){
            throw new OutboundException(OUTBOUND_AMOUNT_OR_LESS_ZERO);
        }

        HashMap<Long, Double> requestStockSum = new HashMap<>(); //요청 Stock 합계
        HashMap<Long, Double> realStockSum = new HashMap<>(); //실제 Stock 합계
        HashMap<Long, Double> productStockSum = new HashMap<>(); //품목별 Stock 합계

        for(var stockRequest : request.getStocks()){
            if(stockRequest.getAmount() == null){
                throw new OutboundException(OUTBOUND_AMOUNT_NULL);
            }

            if(stockRequest.getAmount() <= 0){
                throw new OutboundException(OUTBOUND_AMOUNT_OR_LESS_ZERO);
            }

            StocksDto.StockResponseLot stockResponse = stocksService.getStock(stockRequest.getStockId());
            ProductDto.ProductResponse productResponse = productService.getProduct(stockResponse.getProductId());

            Double requestSum = requestStockSum.getOrDefault(stockResponse.getId(), 0D);
            requestStockSum.put(stockResponse.getId(), requestSum + stockRequest.getAmount());

            Double realSum = realStockSum.getOrDefault(stockResponse.getId(), 0D);
            realStockSum.put(stockResponse.getId(), realSum + stockResponse.getAmount());

            Double productSum = productStockSum.getOrDefault(stockResponse.getProductId(), 0D);
            productStockSum.put(productResponse.getId(), productSum + stockRequest.getAmount());
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

            if(!requestAmount.equals(requiredAmount)){
                throw new OutboundException(OUTBOUND_DIFFERENT_REQUIRES);
            }

        }
    }

}
