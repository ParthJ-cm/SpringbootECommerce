package com.shop.order_service.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException{
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
