package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StocksErrorType implements ErrorTypeInterface{
    STOCKS_NOT_FOUND("해당되는 재고를 찾을 수 없습니다."),
    STOCKS_AMOUNT_NULL("재고는 비어있을 수 없습니다."),
    STOCKS_NOT_CREATE_OR_LESS_ZERO("0이하의 값은 존재할 수 없습니다."),
    STOCKS_LOT_NULL("LOT값이 비어 있을 수 없습니다."),
    STOCKS_CANT_SPEND_OR_LESS_ZERO("0이하의 값은 재고 출고가 불가능합니다.");
    private final String message;
}
