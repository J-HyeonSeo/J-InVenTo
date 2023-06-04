package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.facade.InboundFacade;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PostMapping("")
    public ResponseEntity<?> executeInbound(){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInbound(){
        return null;
    }

}
