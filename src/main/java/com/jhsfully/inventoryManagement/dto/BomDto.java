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
        private Long pid;
        private Long cid;
        private Double cost;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BomResponse{
        private Long id;
        private Long pid;
        private Long cid;
        private Double cost;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BomTree{
        private Long bid;
        private Long pid;
        private Double cost;
        private List<BomTree> children;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class BomLeaf{
        private Long pid;
        private Double cost;
    }
}
