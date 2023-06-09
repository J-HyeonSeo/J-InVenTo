package com.jhsfully.inventoryManagement.type;

public enum RoleType {
    ROLE_PRODUCT_READ,
    ROLE_BOM_READ,
    ROLE_PURCHASE_READ,
    ROLE_INBOUND_READ,
    ROLE_STOCKS_READ,
    ROLE_OUTBOUND_READ,
    ROLE_PLAN_READ,


    ROLE_PRODUCT_MANEGE,
    ROLE_BOM_MANAGE,
    ROLE_PURCHASE_MANAGE,
    ROLE_INBOUND_MANAGE,
    ROLE_STOCKS_MANAGE,
    ROLE_OUTBOUND_MANAGE,
    ROLE_PLAN_MANAGE;

    public static String REFRESH = "refresh";
}
