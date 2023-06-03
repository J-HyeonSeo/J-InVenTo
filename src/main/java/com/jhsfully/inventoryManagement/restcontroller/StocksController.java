package com.jhsfully.inventoryManagement.restcontroller;

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

    @GetMapping("")
    public ResponseEntity<?> getAllStocks(){
        return ResponseEntity.ok(stocksService.getAllStocks());
    }

    @GetMapping("/{productid}")
    public ResponseEntity<?> getLotByPid(@PathVariable Long productid){
        return ResponseEntity.ok(stocksService.getLotByPid(productid));
    }

    //파서드 구현 이후 구현 할 것!!
    @GetMapping("/lacks")
    public ResponseEntity<?> getLacks(){
        return null;
    }

}
