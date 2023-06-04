package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.time.LocalDateTime;

public class InboundDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InboundOuterAddRequest{
        private Long purchaseId;
        private Double amount;
        private String company;
        private String note;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InboundAddRequest{
        private Long purchaseId;
        private Long stockId;
        private Double amount;
        private String company;
        private String note;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class InboundResponse{
        private Long id;
        private Long purchaseId;
        private String productName; //join으로 가져옴.
        private LocalDateTime inboundAt;
        private LocalDateTime purchasedAt; //join으로 가져옴.
        private Double amount;
        private String company;
        private String note;

    }
}
