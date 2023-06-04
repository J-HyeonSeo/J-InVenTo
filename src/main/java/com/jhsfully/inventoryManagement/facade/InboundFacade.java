package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.service.InboundInterface;
import com.jhsfully.inventoryManagement.service.PurchaseInterface;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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

}
