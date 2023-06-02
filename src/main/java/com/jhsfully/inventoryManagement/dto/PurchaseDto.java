package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.time.LocalDateTime;

public class PurchaseDto {

    @Getter
    @Setter
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
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseResponse{
        private Long id;
        private Long productId;
        private LocalDateTime at;
        private Double amount;
        private Double price;
        private String company;
        private String note;
    }
}