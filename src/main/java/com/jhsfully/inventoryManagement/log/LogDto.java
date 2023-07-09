package com.jhsfully.inventoryManagement.log;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class LogDto {

    @Data
    @Builder
    static class AddRequest{
        private String username;
        private String signature;
        private String requestUrl;
        private String method;
        private Long elapsed;
        private LocalDateTime at;
        private boolean is_success;
    }

    @Data
    @Builder
    static class Response{
        private Long id;
        private String username;
        private String signature;
        private String requestUrl;
        private String method;
        private Long elapsed;
        private LocalDateTime at;
        private boolean is_success;

        public static Response of(LogEntity log){
            return Response.builder()
                    .id(log.getId())
                    .username(log.getUsername())
                    .signature(log.getSignature())
                    .requestUrl(log.getRequestUrl())
                    .method(log.getMethod())
                    .elapsed(log.getElapsed())
                    .at(log.getAt())
                    .is_success(log.is_success())
                    .build();
        }
    }
}
