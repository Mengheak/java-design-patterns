package com.pisethjavaschool.good;

import com.pisethjavaschool.model.NotificationRequest;

public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.SMS;
    }

    @Override
    public String send(NotificationRequest request) {
        return "Send SMS to " + request.receiver() + ": " + request.message();
    }
}