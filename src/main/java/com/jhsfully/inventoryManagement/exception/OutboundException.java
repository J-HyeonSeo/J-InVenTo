package com.jhsfully.inventoryManagement.exception;

import com.jhsfully.inventoryManagement.type.InboundErrorType;
import com.jhsfully.inventoryManagement.type.OutboundErrorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutboundException extends RuntimeException{
    private OutboundErrorType outboundErrorType;
    private String errorMessage;

    public OutboundException(OutboundErrorType outboundErrorType){
        this.outboundErrorType = outboundErrorType;
        this.errorMessage = outboundErrorType.getMessage();
    }
}
