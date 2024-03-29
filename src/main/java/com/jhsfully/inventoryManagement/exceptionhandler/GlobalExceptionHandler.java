package com.jhsfully.inventoryManagement.exceptionhandler;

import com.jhsfully.inventoryManagement.dto.ErrorResponse;
import com.jhsfully.inventoryManagement.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuthException(AuthException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getAuthErrorType(), e.getErrorMessage()));
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(ProductException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getProductErrorType(), e.getErrorMessage()));
    }

    @ExceptionHandler(BomException.class)
    public ResponseEntity<ErrorResponse> handleBomException(BomException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getBomErrorType(), e.getErrorMessage()));
    }

    @ExceptionHandler(PurchaseException.class)
    public ResponseEntity<ErrorResponse> handlePurchaseException(PurchaseException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getPurchaseErrorType(), e.getErrorMessage()));
    }

    @ExceptionHandler(StocksException.class)
    public ResponseEntity<ErrorResponse> handleStocksException(StocksException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getStocksErrorType(), e.getErrorMessage()));
    }

    @ExceptionHandler(PlanException.class)
    public ResponseEntity<ErrorResponse> handlePlanException(PlanException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getPlanErrorType(), e.getErrorMessage()));
    }

    @ExceptionHandler(InboundException.class)
    public ResponseEntity<ErrorResponse> handleInboundException(InboundException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getInboundErrorType(), e.getErrorMessage()));
    }

    @ExceptionHandler(OutboundException.class)
    public ResponseEntity<ErrorResponse> handleOutboundException(OutboundException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getOutboundErrorType(), e.getErrorMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleExtrasException(Exception e){
//        return ResponseEntity.internalServerError().body(new ErrorResponse(null, "서버에 오류가 발생하였습니다."));
//    }

}
