package com.pisethjava.decorator.notification;

import java.util.concurrent.atomic.AtomicInteger;

public final class UnstableNotificationSender implements NotificationSender {

    private final AtomicInteger attempts = new AtomicInteger();
    private final int failUntilAttempt;

    public UnstableNotificationSender(int failUntilAttempt) {
        this.failUntilAttempt = failUntilAttempt;
    }

    @Override
    public void send(String message) {
        int attempt = attempts.incrementAndGet();

        if (attempt <= failUntilAttempt) {
            throw new IllegalStateException("Temporary notification failure");
        }

        System.out.println("UNSTABLE SENDER SUCCESS: " + message);
    }
}
