package com.shop.product_service.context;

import org.springframework.stereotype.Component;

@Component
public class UserContextHolder {
    private static final ThreadLocal<UserContext> contextHolder = new ThreadLocal<>();

    public void setUserContext(UserContext context) {
        contextHolder.set(context);
    }

    public UserContext getUserContext() {
        return contextHolder.get();
    }

    public void clear() {
        contextHolder.remove();
    }
}

