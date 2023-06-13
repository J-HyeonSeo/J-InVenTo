package com.jhsfully.inventoryManagement.dto;

import lombok.*;

import java.util.List;

public class BomDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BomAddRequest{
        private Long pid;
        private Long cid;
        private Double cost;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BomUpdateRequest{
        private Long id;
        private Double cost;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BomTopResponse{
        private Long productId;
        private String productName;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BomResponse{
        private Long id;
        private Long pid;
        private String parentName;
        private Long cid;
        private String childName;
        private Double cost;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BomTreeResponse{
        private Long id;
        private Long productId;
        private String productName;
        private Double cost;
        private List<BomTreeResponse> children;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class BomLeaf{
        private Long productId;
        private String productName;
        private Double cost;
    }
}
