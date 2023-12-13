package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.dto.InboundDto.InboundResponse;
import com.jhsfully.inventoryManagement.facade.InboundFacade;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
import com.jhsfully.inventoryManagement.service.InboundInterface;
import com.jhsfully.inventoryManagement.type.LockType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/inbound")
public class InboundController {

    private final InboundFacade inboundFacade;
    private final InboundInterface inboundService;

    @GetMapping("")
    @PreAuthorize("hasRole('INBOUND_READ')")
    public ResponseEntity<?> getInbounds(
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate)
    {
        return ResponseEntity.ok(inboundService.getInbounds(startDate, endDate));
    }

    @GetMapping("/{purchaseId}")
    @PreAuthorize("hasRole('INBOUND_READ')")
    public ResponseEntity<?> getSumInboundsByPurchase(@PathVariable Long purchaseId){
        return ResponseEntity.ok(inboundService.getInboundsByPurchase(purchaseId));
    }

    @ProcessLock(group = LockType.PURCHASE_INBOUND, key = "#request.purchaseId")
    @PostMapping("")
    @PreAuthorize("hasRole('INBOUND_MANAGE')")
    public ResponseEntity<?> executeInbound(@RequestBody InboundDto.InboundOuterAddRequest request){
        return ResponseEntity.ok(inboundFacade.executeInbound(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INBOUND_MANAGE')")
    public ResponseEntity<?> cancelInbound(@PathVariable Long id){
        InboundResponse inboundResponse = inboundService.getInbound(id);
        inboundFacade.cancelInbound(id, inboundResponse.getStockId());
        return ResponseEntity.ok(id);
    }

}
