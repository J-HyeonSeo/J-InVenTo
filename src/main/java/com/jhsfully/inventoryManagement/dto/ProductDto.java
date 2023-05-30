package com.jhsfully.inventoryManagement.dto;

import lombok.*;


public class ProductDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductAddRequest{
        private String name;
        private String company;
        private double price;
        private String spec;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductUpdateRequest{
        private Long id;
        private String name;
        private String company;
        private double price;
        private String spec;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductResponse {
        private long id;
        private String name;
        private String company;
        private double price;
        private String spec;
    }

}
