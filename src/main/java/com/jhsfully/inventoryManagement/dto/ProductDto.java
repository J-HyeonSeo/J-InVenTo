package com.jhsfully.inventoryManagement.dto;

import lombok.*;


public class ProductDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductAddRequest{
        private String name;
        private String company;
        private Double price;
        private String spec;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductUpdateRequest{
        private Long id;
        private String name;
        private String company;
        private Double price;
        private String spec;
        private Boolean enabled;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductResponse {
        private Long id;
        private String name;
        private String company;
        private Double price;
        private String spec;
        private boolean enabled;
    }

}
