package com.pisethjavaschool.good;

import com.pisethjavaschool.model.NotificationRequest;

public class NotificationService {

    private final NotificationStrategyFactory factory;

    public NotificationService(NotificationStrategyFactory factory) {
        this.factory = factory;
    }

    public String send(NotificationChannel channel, NotificationRequest request) {
        NotificationStrategy strategy = factory.getStrategy(channel);
        return strategy.send(request);
    }
}