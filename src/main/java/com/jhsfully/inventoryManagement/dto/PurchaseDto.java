package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.time.LocalDateTime;

public class PurchaseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseAddRequest{
        private Long productId;
        private Double amount;
        private Double price;
        private String company;
        private String note;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseResponse{
        private Long id;
        private Long productId;
        private String productName;
        private LocalDateTime at;
        private Double amount;
        private Double canAmount;
        private Double price;
        private String company;
        private String note;
    }
}
