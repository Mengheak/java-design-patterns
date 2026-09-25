package com.pisethjava.decorator.notification;

public class SmsNotificationSender implements NotificationSender{

	@Override
	public void send(String message) {
		System.out.println("SMS: " + message);
	}

}
