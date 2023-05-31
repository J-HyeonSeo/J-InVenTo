package com.jhsfully.inventoryManagement.exception;

import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.*;

@Getter
@Setter
public class ProductException extends RuntimeException{
    private ProductErrorType productErrorType;
    private String errorMessage;

    public ProductException(ProductErrorType errorType){
        this.productErrorType = errorType;
        this.errorMessage = errorType.getMessage();
    }
}
