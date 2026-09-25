package com.pisethjavaschool.good;

import com.pisethjavaschool.model.NotificationRequest;

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
