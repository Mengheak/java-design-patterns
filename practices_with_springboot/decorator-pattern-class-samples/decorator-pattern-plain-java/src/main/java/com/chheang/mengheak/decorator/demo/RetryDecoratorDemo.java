package com.chheang.mengheak.decorator.demo;

import com.chheang.mengheak.decorator.notification.NotificationSender;
import com.chheang.mengheak.decorator.notification.RetryNotificationDecorator;
import com.chheang.mengheak.decorator.notification.UnstableNotificationSender;

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
