package com.shop.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardResponse<T> {
    private int status;
    private T data;
    private String message;
    private String exception;
}
