package com.shop.product_service.constants;

public final class MessageConstants {
    private MessageConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String build(CommonMessageTemplate template, Object... args) {
        try {
            return template.format(args);
        } catch (Exception e) {
            return "Invalid message format for template: " + template.name();
        }
    }

    public static String exists(String entity, String name, String value){
        return build(CommonMessageTemplate.EXISTS, entity, name, value);
    }

    public static String notFound(String entity, Long id){
        return build(CommonMessageTemplate.NOT_FOUND, entity, id);
    }

    public static String isRequired(String name, String value){
        return build(CommonMessageTemplate.IS_REQUIRED, name, value);
    }
}