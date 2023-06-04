package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseErrorType implements ErrorTypeInterface{
    PURCHASE_NOT_FOUND("구매이력을 찾을 수 없습니다."),
    PURCHASE_AMOUNT_NULL("수량이 존재하지 않습니다."),
    PURCHASE_AMOUNT_LESS_ZERO("수량은 0보다 커야합니다."),
    PURCHASE_PRICE_NULL("단가가 존재하지 않습니다."),
    PURCHASE_PRICE_LESS_ZERO("단가는 0보다 커야합니다."),
    PURCHASE_HAS_INBOUND("입고된 구매이력은 삭제할 수 없습니다.");
    private final String message;
}
