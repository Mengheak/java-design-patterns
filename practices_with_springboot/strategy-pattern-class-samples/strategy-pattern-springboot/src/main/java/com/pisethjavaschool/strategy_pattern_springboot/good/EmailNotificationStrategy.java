package com.pisethjavaschool.strategy_pattern_springboot.good;

import org.springframework.stereotype.Component;

@Component
public class EmailNotificationStrategy implements NotificationStrategy {

    @Override
    public String type() {
        return "EMAIL";
    }

    @Override
    public String send(String message) {
        return "Email sent: " + message;
    }
}