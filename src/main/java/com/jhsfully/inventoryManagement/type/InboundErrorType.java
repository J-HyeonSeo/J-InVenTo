package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboundErrorType implements ErrorTypeInterface{
    INBOUND_EXCEED_PURCHASE_AMOUNT("입고 수량이 구매 수량 보다 많습니다."),
    INBOUND_NOT_FOUND("입고 내역을 찾을 수 없습니다.");
    private final String message;
}
