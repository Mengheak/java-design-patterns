package com.pisethjavaschool.strategy_pattern_springboot.good;

import org.springframework.stereotype.Component;

@Component
public class TelegramNotificationStrategy implements NotificationStrategy {

    @Override
    public String type() {
        return "TELEGRAM";
    }

    @Override
    public String send(String message) {
        return "Telegram sent: " + message;
    }
}