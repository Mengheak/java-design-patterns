package com.pisethjava.decorator.demo;

import com.pisethjava.decorator.notification.*;

public final class DecoratorOrderDemo {

    private DecoratorOrderDemo() {
    }

    public static void run() {
        System.out.println("\n=== 4. DECORATOR ORDER ===");

        NotificationSender loggingOutside =
                new LoggingNotificationDecorator(
                        new EncryptionNotificationDecorator(
                                new EmailNotificationSender()
                        )
                );

        System.out.println("\nLogging outside encryption:");
        loggingOutside.send("Sensitive message");

        NotificationSender encryptionOutside =
                new EncryptionNotificationDecorator(
                        new LoggingNotificationDecorator(
                                new EmailNotificationSender()
                        )
                );

        System.out.println("\nEncryption outside logging:");
        encryptionOutside.send("Sensitive message");
    }
}
