package com.jhsfully.inventoryManagement.exceptionhandler;

import com.jhsfully.inventoryManagement.dto.ErrorResponse;
import com.jhsfully.inventoryManagement.exception.BomException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(ProductException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getProductErrorType(), e.getErrorMessage()));
    }

    @ExceptionHandler(BomException.class)
    public ResponseEntity<ErrorResponse> handleBomException(BomException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getBomErrorType(), e.getErrorMessage()));
    }
}
