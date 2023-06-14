package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
        private List<stock> stocks;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class stock{
        private Long stockId;
        private Double amount;
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
        private String productName;
        private String destination;
        private Double amount;
        private Double price;
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
        private String productName;
        private String company;
        private Double amount;
        private Double price;

    }
}
