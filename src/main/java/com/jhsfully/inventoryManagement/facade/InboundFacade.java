package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.service.InboundInterface;
import com.jhsfully.inventoryManagement.service.OutboundInterface;
import com.jhsfully.inventoryManagement.service.PurchaseInterface;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import com.jhsfully.inventoryManagement.type.InboundErrorType;
import com.jhsfully.inventoryManagement.type.StocksErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

//서비스간 참조를 막기위한 중간층.
//Controller => Facade => Services => Repositories
@Component
@AllArgsConstructor
public class InboundFacade {

    private final InboundInterface inboundService;
    private final OutboundInterface outboundService;
    private final PurchaseInterface purchaseService;
    private final StocksInterface stocksService;

    public List<InboundDto.InboundResponse> getInbounds(LocalDate startDate, LocalDate endDate){
        return inboundService.getInbounds(startDate, endDate);
    }

    @Transactional
    public Long executeInbound(InboundDto.InboundOuterAddRequest request){

        PurchaseDto.PurchaseResponse purchase = validateExecute(request);

        //Stock을 만들어야함.(stockService)
        StocksDto.StockResponseLot stock = stocksService.addStock(
                StocksDto.StockAddRequest.builder()
                        .productId(purchase.getProductId())
                        .amount(request.getAmount())
                        .lot(LocalDate.now())
                        .build()
        );

        //만들어진 Stock을 가지고, 입고 내역을 추가해야함.(inboundService)
        InboundDto.InboundResponse inbound = inboundService.addInbound(
                InboundDto.InboundAddRequest.builder()
                        .purchaseId(purchase.getId())
                        .stockId(stock.getId())
                        .amount(request.getAmount())
                        .note(request.getNote())
                        .build()
        );

        //만들어진 inbound를 stock에 할당해주어야함.
        stocksService.setInbound(stock.getId(), inbound.getId());

        return inbound.getId();
    }

    @Transactional
    public void cancelInbound(Long id){
        //입고를 지운다. => 재고를 지운다. (조건 : 출고 이력이 없어야 함)
        InboundDto.InboundResponse inboundResponse = inboundService.getInbound(id);
        StocksDto.StockResponseLot stockResponse = stocksService.getStock(
                inboundResponse.getStockId()
        );

        //출고 이력 미존재 여부 검증
        if(outboundService.countByStock(stockResponse.getId()) > 0){
            throw new StocksException(StocksErrorType.STOCKS_OCCURRED_OUTBOUND);
        }

        //cascade옵션을 지정하지 않았기에, 수동 할당해제 후 삭제해야함.
        stocksService.releaseInbound(stockResponse.getId());

        inboundService.deleteInbound(inboundResponse.getId());
        stocksService.deleteStock(stockResponse.getId());

    }
    //========================= Validates ==================================
    @Transactional
    public PurchaseDto.PurchaseResponse validateExecute(InboundDto.InboundOuterAddRequest request){
        //구매번호에대한 입고된 합이, 구매수량 이하여야 함. (구매 수량을 가져옴)
        PurchaseDto.PurchaseResponse purchase =
                purchaseService.getPurchase(request.getPurchaseId());

        //해당 되는 구매ID의 입고합을 가져와야함. (inboundService)
        List<InboundDto.InboundResponse> inbounds = inboundService.getInboundsByPurchase(
                purchase.getId()
        );

        double inboundSum = inbounds.stream()
                .mapToDouble(InboundDto.InboundResponse::getAmount)
                .sum();

        //현재 입고수량을 더했을 경우, 구매량을 초과하는지 검증해야함.
        inboundSum += request.getAmount();

        if(purchase.getAmount() < inboundSum){
            throw new InboundException(InboundErrorType.INBOUND_EXCEED_PURCHASE_AMOUNT);
        }
        return purchase;
    }
}
