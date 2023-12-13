package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
import com.jhsfully.inventoryManagement.service.InboundInterface;
import com.jhsfully.inventoryManagement.service.OutboundInterface;
import com.jhsfully.inventoryManagement.service.PurchaseInterface;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import com.jhsfully.inventoryManagement.type.InboundErrorType;
import com.jhsfully.inventoryManagement.type.LockType;
import com.jhsfully.inventoryManagement.type.StocksErrorType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//서비스간 참조를 막기위한 중간층.
//Controller => Facade => Services => Repositories
@Component
@Transactional
@AllArgsConstructor
public class InboundFacade {

    private final InboundInterface inboundService;
    private final OutboundInterface outboundService;
    private final PurchaseInterface purchaseService;
    private final StocksInterface stocksService;


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

    @ProcessLock(group = LockType.INBOUND_OUTBOUND, key = "#stockId")
    public void cancelInbound(Long inboundId, Long stockId){

        //출고 이력 미존재 여부 검증
        if(outboundService.countByStock(stockId) > 0){
            throw new StocksException(StocksErrorType.STOCKS_OCCURRED_OUTBOUND);
        }

        //cascade옵션을 지정하지 않았기에, 수동 할당해제 후 삭제해야함.
        stocksService.releaseInbound(stockId);

        inboundService.deleteInbound(inboundId);
        stocksService.deleteStock(stockId);

    }


    //========================= Validates ==================================

    private PurchaseDto.PurchaseResponse validateExecute(InboundDto.InboundOuterAddRequest request){
        PurchaseDto.PurchaseResponse purchase =
                purchaseService.getPurchase(request.getPurchaseId());

        Double canInboundAmount = purchase.getCanAmount();

        if(request.getAmount() > canInboundAmount){
            throw new InboundException(InboundErrorType.INBOUND_EXCEED_PURCHASE_AMOUNT);
        }
        return purchase;
    }
}
