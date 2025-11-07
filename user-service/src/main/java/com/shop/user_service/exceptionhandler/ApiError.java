package com.shop.user_service.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {
    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();
    private int statusCode;
    private String error;
    private String message;
    private Map<String,String> details;
}
