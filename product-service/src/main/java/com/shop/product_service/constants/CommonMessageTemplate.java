package com.shop.product_service.constants;

public enum CommonMessageTemplate {
    EXISTS("%s with name %s already exists."),
    NOT_FOUND("%s with id %d not found");

    private final String template;

    CommonMessageTemplate(String template){
        this.template = template;
    }

    public String format(Object... args){
        return String.format(template, args);
    }
}