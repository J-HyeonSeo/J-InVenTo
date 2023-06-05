package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboundErrorType implements ErrorTypeInterface{
    OUTBOUND_NOT_FOUND("출고이력을 찾을 수 없습니다."),
    OUTBOUND_DETAILS_NOT_FOUND("출고 상세 이력을 찾을 수 없습니다.");
    private final String message;
}
