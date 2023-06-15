package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.facade.OutboundFacade;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
import com.jhsfully.inventoryManagement.service.OutboundInterface;
import com.jhsfully.inventoryManagement.type.LockType;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/outbound")
public class OutboundController {

    private final OutboundFacade outboundFacade;
    private final OutboundInterface outboundService;

    @GetMapping("")
    public ResponseEntity<?> getOutbounds(
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate
    ){
        return ResponseEntity.ok(outboundService.getOutbounds(startDate, endDate));
    }

    @GetMapping("/{outboundId}")
    public ResponseEntity<?> getOutboundDetails(@PathVariable Long outboundId){
        return ResponseEntity.ok(outboundService.getOutboundDetails(outboundId));
    }

    @ProcessLock(key = LockType.INBOUND_OUTBOUND)
    @PostMapping("")
    public ResponseEntity<?> executeOutbound(
            @RequestBody OutboundDto.OutboundAddRequest request
            ){
        return ResponseEntity.ok(outboundFacade.executeOutbound(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOutbound(@PathVariable Long id){
        outboundFacade.cancelOutbound(id);
        return ResponseEntity.ok(id);
    }

    //출고 상세 삭제 기능은 일단 보류함.
    @DeleteMapping("/detail/{detailId}")
    public ResponseEntity<?> cancelOutboundDetail(@PathVariable Long detailId){
        outboundFacade.cancelOutboundDetail(detailId);
        return ResponseEntity.ok(detailId);
    }

}
