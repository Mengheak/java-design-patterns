package com.pisethjava.decorator.notification;

public final class RetryNotificationDecorator extends NotificationDecorator {

    private final int maxAttempts;

    public RetryNotificationDecorator(
            NotificationSender delegate,
            int maxAttempts
    ) {
        super(delegate);

        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1");
        }

        this.maxAttempts = maxAttempts;
    }

    @Override
    public void send(String message) {
        RuntimeException lastFailure = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("[RETRY] attempt=" + attempt);
                delegate.send(message);
                return;
            } catch (RuntimeException exception) {
                lastFailure = exception;
            }
        }

        throw lastFailure;
    }
}
