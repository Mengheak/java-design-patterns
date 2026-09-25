package com.pisethjava.decorator.notification;

public final class EncryptionNotificationDecorator extends NotificationDecorator {

    public EncryptionNotificationDecorator(NotificationSender delegate) {
        super(delegate);
    }

    @Override
    public void send(String message) {
        delegate.send(encrypt(message));
    }

    private String encrypt(String message) {
        return "ENCRYPTED[" + message + "]";
    }
}
