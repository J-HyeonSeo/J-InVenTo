package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BomErrorType implements ErrorTypeInterface{
    BOM_NOT_FOUND("삭제할 BOM이 존재하지 않습니다."),
    HAS_SAME_PARENT("서로 같은 부모를 포함할 수 없습니다."),
    COST_MINUS("음수 비용은 포함할 수 없습니다.");
    private final String message;
}
