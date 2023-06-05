package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.facade.OutboundFacade;
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
        return null;
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getOutboundDetails(@PathVariable Long id){
        return null;
    }

    @PostMapping("")
    public ResponseEntity<?> executeOutbound(){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOutbound(@PathVariable Long id){
        return null;
    }

    //출고 상세 삭제 기능은 일단 보류함.
    @DeleteMapping("/detail/{id}")
    public ResponseEntity<?> deleteOutboundDetail(@PathVariable Long detailId){
        return null;
    }

}
