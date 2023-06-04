package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.time.LocalDate;

public class PlanDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlanAddRequest{
        private Long productId;
        private LocalDate due;
        private String destination;
        private Double amount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlanUpdateRequest{
        private Long id;
        private LocalDate due;
        private String destination;
        private Double amount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlanResponse{
        private Long id;
        private Long productId;
        private LocalDate due;
        private String destination;
        private Double amount;
    }
}
