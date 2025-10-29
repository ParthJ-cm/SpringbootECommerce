package com.shop.user_service.error;

public class InvalidToken extends RuntimeException {
    public InvalidToken(String message) {
        super(message);
    }
}

