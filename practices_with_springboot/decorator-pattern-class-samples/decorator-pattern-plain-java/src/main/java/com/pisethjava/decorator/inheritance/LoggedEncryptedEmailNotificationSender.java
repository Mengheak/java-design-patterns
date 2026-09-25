package com.pisethjava.decorator.inheritance;

public class LoggedEncryptedEmailNotificationSender {

    public void send(String message) {
        System.out.println("[LOG] before");
        System.out.println("EMAIL: ENCRYPTED[" + message + "]");
        System.out.println("[LOG] after");
    }
}
