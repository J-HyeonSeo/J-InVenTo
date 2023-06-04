package com.jhsfully.inventoryManagement.exception;

import com.jhsfully.inventoryManagement.type.InboundErrorType;
import com.jhsfully.inventoryManagement.type.PlanErrorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InboundException extends RuntimeException{
    private InboundErrorType inboundErrorType;
    private String errorMessage;

    public InboundException(InboundErrorType inboundErrorType){
        this.inboundErrorType = inboundErrorType;
        this.errorMessage = inboundErrorType.getMessage();
    }
}
