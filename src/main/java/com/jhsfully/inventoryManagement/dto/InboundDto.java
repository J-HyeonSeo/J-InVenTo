package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.time.LocalDateTime;

public class InboundDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InboundOuterAddRequest{
        private Long purchaseId;
        private Double amount;
        private String note;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InboundAddRequest{
        private Long purchaseId;
        private Long stockId;
        private Double amount;
        private String note;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InboundResponse{
        private Long id;
        private Long stockId;
        private Long purchaseId;
        private String productName;
        private LocalDateTime inboundAt;
        private LocalDateTime purchasedAt;
        private Double amount;
        private Double price;
        private String company;
        private String note;

    }
}
