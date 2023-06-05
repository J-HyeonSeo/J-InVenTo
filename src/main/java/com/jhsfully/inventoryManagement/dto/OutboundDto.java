package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.time.LocalDateTime;

public class OutboundDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OutboundAddRequest{

        private Long productId;
        private String destination;
        private Double amount;
        private String note;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OutboundDetailAddRequest{

        private Long outboundId;
        private Long stockId;
        private Double amount;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OutboundResponse{

        private Long id;
        private String productName; //join
        private String destination;
        private Double amount;
        private LocalDateTime at;
        private String note;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OutboundDetailResponse{

        private Long id;
        private Long stockId;
        private String productName; //join
        private Double amount;

    }
}
