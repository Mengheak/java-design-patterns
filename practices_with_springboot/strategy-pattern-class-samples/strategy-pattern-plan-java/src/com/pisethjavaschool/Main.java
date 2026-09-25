package com.pisethjavaschool;

import java.util.List;

import com.pisethjavaschool.good.EmailNotificationStrategy;
import com.pisethjavaschool.good.NotificationChannel;
import com.pisethjavaschool.good.NotificationService;
import com.pisethjavaschool.good.NotificationStrategy;
import com.pisethjavaschool.good.NotificationStrategyFactory;
import com.pisethjavaschool.good.SlackNotificationStrategy;
import com.pisethjavaschool.good.SmsNotificationStrategy;
import com.pisethjavaschool.good.TelegramNotificationStrategy;
import com.pisethjavaschool.model.NotificationRequest;

public class Main {

	public static void main(String[] args) {
		NotificationRequest request = new NotificationRequest(
                "student@example.com",
                "Welcome to Design Pattern class!"
        );

        //System.out.println("=== Bad Version: if / else ===");
        //BadNotificationService badService = new BadNotificationService();
        //System.out.println(badService.send("EMAIL", request));
        //System.out.println(badService.send("SMS", request));
        //System.out.println(badService.send("SLACK", request));
		
		System.out.println();
        System.out.println("=== Good Version: Strategy Pattern ===");

        List<NotificationStrategy> strategies = List.of(
                new EmailNotificationStrategy(),
                new SmsNotificationStrategy(),
                new TelegramNotificationStrategy(),
                new SlackNotificationStrategy()
        );

        NotificationStrategyFactory factory = new NotificationStrategyFactory(strategies);
        NotificationService notificationService = new NotificationService(factory);

        //System.out.println(notificationService.send(NotificationChannel.EMAIL, request));
        //System.out.println(notificationService.send(NotificationChannel.SMS, request));
        //System.out.println(notificationService.send(NotificationChannel.TELEGRAM, request));
        System.out.println(notificationService.send(NotificationChannel.SLACK, request));

        
        


	}

}
