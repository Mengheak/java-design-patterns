package com.chheang.mengheak.good;

import com.chheang.mengheak.model.NotificationRequest;

public class SlackNotificationStrategy implements NotificationStrategy {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.SLACK;
    }

    @Override
    public String send(NotificationRequest request) {
        return "Send SLACK to " + request.receiver() + ": " + request.message();
    }
}
