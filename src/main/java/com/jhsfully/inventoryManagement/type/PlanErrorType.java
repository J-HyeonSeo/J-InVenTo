package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanErrorType implements ErrorTypeInterface{
    PLAN_DUE_IS_NULL("출고 예정일이 비어있습니다."),
    PLAN_DUE_BEFORE_TODAY("출고 예정일이 오늘보다 앞설 수 없습니다.");
    private final String message;
}
