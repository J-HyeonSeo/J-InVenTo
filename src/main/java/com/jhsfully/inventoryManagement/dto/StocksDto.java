package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.time.LocalDate;

public class StocksDto{

    @Getter
    @AllArgsConstructor
    @Builder
    public static class StockAddRequest{
        private Long productId;
        private Double amount;
        private LocalDate lot;
        private String company;
    }

    @Getter
    @AllArgsConstructor
    public static class StockGroupResponse{ //Use Inner Dto
        private Long productId;
        private Double amount;
        private Double price;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class StockResponse{ //Client Return Dto
        private Long productId;
        private String productName;
        private String spec;
        private Double amount;
        private Double price;
        private LocalDate lackDate;
        private Double lackAmount;

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class StockResponseLot{
        private Long id;
        private Long productId;
        private String productName;
        private Double amount;
        private Double price;
        private LocalDate lot;
        private String company;
    }

}
