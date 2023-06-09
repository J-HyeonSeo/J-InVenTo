package com.jhsfully.inventoryManagement.exception;

import com.jhsfully.inventoryManagement.type.AuthErrorType;
import com.jhsfully.inventoryManagement.type.BomErrorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthException extends RuntimeException{
    private AuthErrorType authErrorType;
    private String errorMessage;

    public AuthException(AuthErrorType authErrorType){
        this.authErrorType = authErrorType;
        this.errorMessage = authErrorType.getMessage();
    }
}
