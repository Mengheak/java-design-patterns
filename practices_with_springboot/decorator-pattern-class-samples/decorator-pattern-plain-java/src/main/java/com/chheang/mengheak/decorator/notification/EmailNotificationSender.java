package com.chheang.mengheak.decorator.notification;

public final class EmailNotificationSender implements NotificationSender {

    @Override
    public void send(String message) {
        System.out.println("EMAIL: " + message);
    }
}
