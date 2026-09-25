package com.pisethjava.decorator.inheritance;

public class LoggedEmailNotificationSender {

    public void send(String message) {
        System.out.println("[LOG] before");
        System.out.println("EMAIL: " + message);
        System.out.println("[LOG] after");
    }
}
