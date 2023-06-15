package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorType implements ErrorTypeInterface{
    AUTH_REGISTER_EXISTS_USERNAME("해당 USERNAME이 이미 존재합니다."),
    AUTH_LOGIN_FAILED("USERNAME이 없거나 잘못되었거나, PASSWORD가 없거나 잘못되었습니다."),
    AUTH_SECURITY_ERROR("REFRESH 토큰으로는 ACCESS가 불가능합니다."),
    AUTH_CHANGE_PASSWORD_NULL_OR_EMPTY("변경할 PASSWORD가 비어있습니다."),
    AUTH_NOT_DEFINITION_ROLE_TYPE("정의되지 않은 권한입니다."),
    AUTH_PASSWORD_IS_NULL_OR_EMPTY("PASSWORD가 비어있습니다."),
    AUTH_DEPARTMENT_IS_NULL_OR_EMPTY("소속이 비어있습니다."),
    AUTH_NAME_IS_NULL_OR_EMPTY("이름이 비어있습니다."),
    AUTH_USERNAME_IS_NULL_OR_EMPTY("USERNAME 이 비어있습니다.");
    private final String message;
}
