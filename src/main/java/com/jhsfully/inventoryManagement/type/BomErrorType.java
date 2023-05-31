package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BomErrorType implements ErrorTypeInterface{
    HAS_SAME_PARENT("서로 같은 부모를 포함할 수 없습니다."),
    COST_MINUS("음수 비용은 포함할 수 없습니다.");
    private final String message;
}
