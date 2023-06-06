package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.facade.StocksFacade;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllStocks(){
        return ResponseEntity.ok(stocksFacade.getAllStocks());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getLotByPid(@PathVariable Long productId){
        return ResponseEntity.ok(stocksService.getLotByPid(productId));
    }

}
