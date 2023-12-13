package com.jhsfully.inventoryManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
