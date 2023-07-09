package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.facade.StocksFacade;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import com.jhsfully.inventoryManagement.type.StocksErrorType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/stocks")
public class StocksController {

    private final StocksInterface stocksService;
    private final StocksFacade stocksFacade;

    //파서드에서 가져옴.
    @GetMapping("")
    @PreAuthorize("hasRole('STOCKS_READ')")
    public ResponseEntity<?> getAllStocks(){
        return ResponseEntity.ok(stocksFacade.getAllStocks());
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('STOCKS_READ')")
    public ResponseEntity<?> getLotByPid(@PathVariable Long productId){
        return ResponseEntity.ok(stocksService.getLotByPid(productId));
    }

}
