package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboundErrorType implements ErrorTypeInterface{
    OUTBOUND_NOT_FOUND("출고이력을 찾을 수 없습니다."),
    OUTBOUND_DETAILS_NOT_FOUND("출고 상세 이력을 찾을 수 없습니다."),
    OUTBOUND_EXCEED_STOCKS("출고 수량이 재고수량을 초과합니다."),
    OUTBOUND_DIFFERENT_REQUIRES("BOM에 정의된 수량과 다릅니다."),
    OUTBOUND_AMOUNT_NULL("출고 수량이 존재하지 않습니다."),
    OUTBOUND_AMOUNT_OR_LESS_ZERO("출고 수량은 0보다 커야합니다.");
    private final String message;
}
