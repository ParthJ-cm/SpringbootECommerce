package com.shop.product_service.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends BaseException{
    public EntityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}