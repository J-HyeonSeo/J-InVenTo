package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.jhsfully.inventoryManagement.facade.InboundFacade;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
import com.jhsfully.inventoryManagement.type.LockType;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.concurrent.locks.Lock;

@RestController
@AllArgsConstructor
@RequestMapping("/inbound")
public class InboundController {

    private final InboundFacade inboundFacade;

    @GetMapping("")
    public ResponseEntity<?> getInbounds(
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate)
    {
        return ResponseEntity.ok(inboundFacade.getInbounds(startDate, endDate));
    }

    @ProcessLock(key = LockType.PURCHASE_INBOUND)
    @PostMapping("")
    public ResponseEntity<?> executeInbound(@RequestBody InboundDto.InboundOuterAddRequest request){
        return ResponseEntity.ok(inboundFacade.executeInbound(request));
    }

    @ProcessLock(key = LockType.INBOUND_OUTBOUND)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelInbound(@PathVariable Long id){
        inboundFacade.cancelInbound(id);
        return ResponseEntity.ok(id);
    }

}
