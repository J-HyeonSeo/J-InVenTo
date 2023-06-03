package com.jhsfully.inventoryManagement.exception;

import com.jhsfully.inventoryManagement.type.PlanErrorType;
import com.jhsfully.inventoryManagement.type.StocksErrorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanException extends RuntimeException{
    private PlanErrorType planErrorType;
    private String errorMessage;

    public PlanException(PlanErrorType planErrorType){
        this.planErrorType = planErrorType;
        this.errorMessage = planErrorType.getMessage();
    }
}
