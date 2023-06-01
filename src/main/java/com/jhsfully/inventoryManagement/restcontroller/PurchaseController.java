package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping("")
    public ResponseEntity<?> getPurchases(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                          @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate){
        return ResponseEntity.ok(purchaseService.getPurchases(startDate, endDate));
    }

    @PostMapping("")
    public ResponseEntity<?> addPurchase(@RequestBody PurchaseDto.PurchaseAddRequest request){
        return ResponseEntity.ok(purchaseService.addPurchase(request));
    }

    @PutMapping("")
    public ResponseEntity<?> updatePurchase(){
        return null;
    }

    @DeleteMapping("")
    public ResponseEntity<?> deletePurchase(){
        return null;
    }

}
