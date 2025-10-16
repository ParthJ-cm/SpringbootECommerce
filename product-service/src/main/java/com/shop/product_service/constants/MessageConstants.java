package com.shop.product_service.constants;

public final class MessageConstants {
    private MessageConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final class Brand{
        private Brand(){
            throw new UnsupportedOperationException();
        }

        public static final String EXISTS = "Brand with name '%s' already exists.";
    }

}