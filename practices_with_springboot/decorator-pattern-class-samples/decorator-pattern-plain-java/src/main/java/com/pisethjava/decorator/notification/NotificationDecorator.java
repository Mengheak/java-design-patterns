package com.pisethjava.decorator.notification;

import java.util.Objects;

public abstract class NotificationDecorator implements NotificationSender {

    protected final NotificationSender delegate;

    protected NotificationDecorator(NotificationSender delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate is required");
    }
}
