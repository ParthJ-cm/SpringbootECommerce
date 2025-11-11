package com.shop.order_service.exceptionHandler;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ApiError {

    private LocalDateTime timeStamp;
    private String error;
    private HttpStatus status;
    private Map<String,String> details;

    public ApiError(){
        this.timeStamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus status){
        this();
        this.error = error;
        this.status = status;
    }

    public ApiError(String error, HttpStatus status, Map<String,String> details){
        this(error, status);
        this.details = details;
    }
}
