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
    STOCKS_CANT_SPEND_OR_LESS_ZERO("0이하의 값은 재고 출고가 불가능합니다."),
    STOCKS_CANT_CANCEL_OR_LESS_ZERO("0이하의 값은 취소가 불가능합니다."),
    STOCKS_SPEND_AMOUNT_EXCEED("차감 하려는 수량이 현재 수량을 초과합니다."),
    STOCKS_CANCEL_SPEND_AMOUNT_EXCEED("차감 하려는 수량이 현재 수량을 초과합니다."),
    STOCKS_OCCURRED_OUTBOUND("해당 재고는 출고이력이 있습니다.");
    private final String message;
}
