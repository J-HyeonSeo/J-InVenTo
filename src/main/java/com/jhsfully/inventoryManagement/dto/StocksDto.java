package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.time.LocalDate;

public class StocksDto{

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StockAddRequest{
        private Long pid;
        private Double amount;
        private LocalDate lot;
        private String company;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class StockResponse{
        private Long pid;
        private String productName;
        private String spec;
        private Double amount;
//        public StockResponse(Long pid, Double amount){
//            this.pid = pid;
//            this.amount = amount;
//        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StockResponseLot{
        private Long id;
        private Double amount;
        private LocalDate lot;
        private String company;
    }

}
