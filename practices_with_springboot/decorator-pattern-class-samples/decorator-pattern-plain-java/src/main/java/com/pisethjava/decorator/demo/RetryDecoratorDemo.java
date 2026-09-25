package com.pisethjava.decorator.demo;

import com.pisethjava.decorator.notification.NotificationSender;
import com.pisethjava.decorator.notification.RetryNotificationDecorator;
import com.pisethjava.decorator.notification.UnstableNotificationSender;

public final class RetryDecoratorDemo {

    private RetryDecoratorDemo() {
    }

    public static void run() {
        System.out.println("\n=== 3. RETRY DECORATOR ===");

        NotificationSender sender =
                new RetryNotificationDecorator(
                        new UnstableNotificationSender(2),
                        3
                );

        sender.send("Retry demo");
    }
}
