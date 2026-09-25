package com.chheang.mengheak.good;

import com.chheang.mengheak.model.NotificationRequest;

public class TelegramNotificationStrategy implements NotificationStrategy {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.TELEGRAM;
    }

    @Override
    public String send(NotificationRequest request) {
        return "Send TELEGRAM to " + request.receiver() + ": " + request.message();
    }
}