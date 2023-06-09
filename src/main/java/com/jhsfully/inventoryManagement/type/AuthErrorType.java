package com.jhsfully.inventoryManagement.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorType implements ErrorTypeInterface{
    AUTH_REGISTER_EXISTS_USERNAME("해당 USERNAME이 이미 존재합니다."),
    AUTH_LOGIN_FAILED("USERNAME이 없거나 잘못되었거나, PASSWORD가 없거나 잘못되었습니다."),
    AUTH_SECURITY_ERROR("REFRESH 토큰으로는 ACCESS가 불가능합니다.");
    private final String message;
}
