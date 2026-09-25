package com.pisethjavaschool.strategy_pattern_springboot.good;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * GOOD EXAMPLE
 * This service does not know HOW to send Email, SMS, Telegram, or Slack.
 * It only selects the correct strategy and delegates the work.
 */
@Service
public class NotificationService {

    private final Map<String, NotificationStrategy> strategies;

    public NotificationService(List<NotificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.type().toUpperCase(),
                        Function.identity()
                        //strategy -> strategy
                ));
    }

    public String send(String type, String message) {
        NotificationStrategy strategy = strategies.get(type.toUpperCase());

        if (strategy == null) {
            throw new UnsupportedNotificationTypeException(type);
        }

        return strategy.send(message);
    }
}