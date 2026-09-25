package com.pisethjava.decorator.demo;

import com.pisethjava.decorator.notification.*;

public final class BasicDecoratorDemo {

    private BasicDecoratorDemo() {
    }

    public static void run() {
       // System.out.println("\n=== 2. BASIC DECORATOR CHAIN ===");
        
        NotificationSender sender =
                new MetricsNotificationDecorator(
                        new LoggingNotificationDecorator(
                                new EncryptionNotificationDecorator(
                                        new EmailNotificationSender()
                                )
                        )
                );

        sender.send("Your booking is confirmed.");
        
        
        //NotificationSender sender = new EmailNotificationSender();
    	//NotificationSender sender = new LoggingNotificationDecorator(new EmailNotificationSender());
    	//NotificationSender sender = new LoggingNotificationDecorator(new SmsNotificationSender());
    	//NotificationSender sender = new EncryptionNotificationDecorator(new SmsNotificationSender());
        
//    	NotificationSender sender = new LoggingNotificationDecorator(
//    			new EncryptionNotificationDecorator(
//    					new SmsNotificationSender()));
    	
    	//System.out.println("[LOG]: sending email.");
        //sender.send("Your booking is confirmed.");
        //System.out.println("[LOG]: Email sent successfully.");
    }
}
