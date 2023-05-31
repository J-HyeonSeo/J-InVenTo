package com.jhsfully.inventoryManagement.dto;

import com.jhsfully.inventoryManagement.type.ErrorTypeInterface;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse{

    private ErrorTypeInterface ErrorType;
    private String ErrorMessage;

}
