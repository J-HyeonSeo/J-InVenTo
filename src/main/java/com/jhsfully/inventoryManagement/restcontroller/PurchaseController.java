package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
import com.jhsfully.inventoryManagement.service.PurchaseInterface;
import com.jhsfully.inventoryManagement.service.PurchaseService;
import com.jhsfully.inventoryManagement.type.LockType;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseInterface purchaseService;

    @GetMapping("")
    @PreAuthorize("hasRole('PURCHASE_READ')")
    public ResponseEntity<?> getPurchases(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                          @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate){
        return ResponseEntity.ok(purchaseService.getPurchases(startDate, endDate));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('PURCHASE_MANAGE')")
    public ResponseEntity<?> addPurchase(@RequestBody PurchaseDto.PurchaseAddRequest request){
        return ResponseEntity.ok(purchaseService.addPurchase(request));
    }

    @ProcessLock(value = LockType.PURCHASE_INBOUND, key = "id")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PURCHASE_MANAGE')")
    public ResponseEntity<?> deletePurchase(@PathVariable Long id){
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok(id);
    }

}
