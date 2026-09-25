package com.pisethjavaschool.good;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class NotificationStrategyFactory {

    private final Map<NotificationChannel, NotificationStrategy> strategies;

    public NotificationStrategyFactory(List<NotificationStrategy> strategyList) {
        this.strategies = new EnumMap<>(NotificationChannel.class);

        for (NotificationStrategy strategy : strategyList) {
            strategies.put(strategy.channel(), strategy);
        }
    }

    public NotificationStrategy getStrategy(NotificationChannel channel) {
        NotificationStrategy strategy = strategies.get(channel);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported notification channel: " + channel);
        }

        return strategy;
    }
}