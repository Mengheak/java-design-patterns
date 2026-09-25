package com.chheang.mengheak.good;

import com.chheang.mengheak.model.NotificationRequest;

public class EmailNotificationStrategy implements NotificationStrategy {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public String send(NotificationRequest request) {
        return "Send EMAIL to " + request.receiver() + ": " + request.message();
    }
}