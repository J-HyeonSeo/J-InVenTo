package com.jhsfully.inventoryManagement.dto;

import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private ProductErrorType ErrorType;
    private String ErrorMessage;

}