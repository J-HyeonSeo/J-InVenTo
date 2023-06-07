package com.jhsfully.inventoryManagement.exception;

import com.jhsfully.inventoryManagement.type.ErrorTypeInterface;
import com.jhsfully.inventoryManagement.type.PurchaseErrorType;
import lombok.Getter;

@Getter
public class PurchaseException extends RuntimeException{

    private ErrorTypeInterface purchaseErrorType;
    private String errorMessage;

    public PurchaseException(PurchaseErrorType purchaseErrorType){
        this.purchaseErrorType = purchaseErrorType;
        this.errorMessage = purchaseErrorType.getMessage();
    }

}
