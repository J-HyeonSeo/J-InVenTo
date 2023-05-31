package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductErrorType implements ErrorTypeInterface{

    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),
    PRODUCT_NAME_NULL("상품명이 비어있습니다."),
    PRODUCT_PRICE_MINUS("단가는 음수가 들어올 수 없습니다."),
    PRODUCT_HAS_BOM("삭제할 품목에 BOM이 형성되어 있습니다.");
    private final String message;

}
