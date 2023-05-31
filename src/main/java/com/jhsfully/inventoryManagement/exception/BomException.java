package com.jhsfully.inventoryManagement.exception;

import com.jhsfully.inventoryManagement.type.BomErrorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BomException extends RuntimeException{
    private BomErrorType bomErrorType;
    private String errorMessage;

    public BomException(BomErrorType bomErrorType){
        this.bomErrorType = bomErrorType;
        this.errorMessage = bomErrorType.getMessage();
    }
}
