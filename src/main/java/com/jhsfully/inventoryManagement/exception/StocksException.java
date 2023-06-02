package com.jhsfully.inventoryManagement.exception;

import com.jhsfully.inventoryManagement.type.BomErrorType;
import com.jhsfully.inventoryManagement.type.StocksErrorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StocksException extends RuntimeException{
    private StocksErrorType stocksErrorType;
    private String errorMessage;

    public StocksException(StocksErrorType stocksErrorType){
        this.stocksErrorType = stocksErrorType;
        this.errorMessage = stocksErrorType.getMessage();
    }
}
