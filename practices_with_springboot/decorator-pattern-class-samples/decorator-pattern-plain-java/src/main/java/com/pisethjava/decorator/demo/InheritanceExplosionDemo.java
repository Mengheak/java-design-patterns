package com.pisethjava.decorator.demo;

import com.pisethjava.decorator.inheritance.LoggedEmailNotificationSender;
import com.pisethjava.decorator.inheritance.LoggedEncryptedEmailNotificationSender;

public final class InheritanceExplosionDemo {

    private InheritanceExplosionDemo() {
    }

    public static void run() {
        System.out.println("\n=== 1. INHERITANCE EXPLOSION ===");

        new LoggedEmailNotificationSender().send("Booking confirmed");
        new LoggedEncryptedEmailNotificationSender().send("Booking confirmed");

        System.out.println(
                "Adding Retry, Metrics, Audit, and RateLimit would create many subclass combinations."
        );
    }
}
