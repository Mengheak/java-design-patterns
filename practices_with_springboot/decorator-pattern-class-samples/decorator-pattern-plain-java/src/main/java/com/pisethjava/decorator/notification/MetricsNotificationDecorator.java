package com.pisethjava.decorator.notification;

public final class MetricsNotificationDecorator extends NotificationDecorator {

    public MetricsNotificationDecorator(NotificationSender delegate) {
        super(delegate);
    }

    @Override
    public void send(String message) {
        long start = System.nanoTime();

        try {
            delegate.send(message);
        } finally {
            long duration = System.nanoTime() - start;
            System.out.println("[METRICS] duration=" + duration + "ns");
        }
    }
}
