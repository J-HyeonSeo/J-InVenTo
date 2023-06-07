package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.jhsfully.inventoryManagement.facade.OutboundFacade;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
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

    @GetMapping("")
    public ResponseEntity<?> getOutbounds(
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate
    ){
        return ResponseEntity.ok(outboundFacade.getOutbounds(startDate, endDate));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getOutboundDetails(@PathVariable Long id){
        return ResponseEntity.ok(outboundFacade.getOutboundDetails(id));
    }

    @ProcessLock(key = "cancelInbound-outbound")
    @PostMapping("")
    public ResponseEntity<?> executeOutbound(
            @RequestBody OutboundDto.OutboundAddRequest request
            ){
        outboundFacade.executeOutbound(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOutbound(@PathVariable Long id){
        outboundFacade.cancelOutbound(id);
        return ResponseEntity.ok(null);
    }

    //출고 상세 삭제 기능은 일단 보류함.
    @DeleteMapping("/detail/{id}")
    public ResponseEntity<?> cancelOutboundDetail(@PathVariable Long detailId){
        outboundFacade.cancelOutboundDetail(detailId);
        return ResponseEntity.ok(null);
    }

}
