package com.pisethjavaschool.strategy_pattern_springboot.good;

import org.springframework.stereotype.Component;

@Component
public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public String type() {
        return "SMS";
    }

    @Override
    public String send(String message) {
        return "SMS sent: " + message;
    }
}