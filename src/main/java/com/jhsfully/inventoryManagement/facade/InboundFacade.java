package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.service.InboundInterface;
import com.jhsfully.inventoryManagement.service.PurchaseInterface;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import com.jhsfully.inventoryManagement.type.InboundErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

//서비스간 참조를 막기위한 중간층.
//Controller => Facade => Services => Repositories
@Component
@AllArgsConstructor
public class InboundFacade {

    private final InboundInterface inboundService;
    private final PurchaseInterface purchaseService;
    private final StocksInterface stocksService;

    public List<InboundDto.InboundResponse> getInbounds(LocalDate startDate, LocalDate endDate){
        return inboundService.getInbounds(startDate, endDate);
    }

    @Transactional
    public Long executeInbound(InboundDto.InboundOuterAddRequest request){
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

        if(purchase.getAmount() > inboundSum){
            throw new InboundException(InboundErrorType.INBOUND_EXCEED_PURCHASE_AMOUNT);
        }

        //Stock을 만들어야함.(stockService)
        StocksDto.StockResponseLot stock = stocksService.addStock(
                StocksDto.StockAddRequest.builder()
                        .productId(purchase.getProductId())
                        .amount(request.getAmount())
                        .lot(LocalDate.now())
                        .company(request.getCompany())
                        .build()
        );

        //만들어진 Stock을 가지고, 입고 내역을 추가해야함.(inboundService)
        InboundDto.InboundResponse inbound = inboundService.addInbound(
                InboundDto.InboundAddRequest.builder()
                        .purchaseId(purchase.getId())
                        .stockId(stock.getId())
                        .amount(request.getAmount())
                        .company(request.getCompany())
                        .note(request.getNote())
                        .build()
        );

        return inbound.getId();
    }

    public void deleteInbound(Long id){
        //출고 이력 여부 확인 필수!!
    }

}
