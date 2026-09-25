package com.pisethjavaschool.strategy_pattern_springboot.good;

import org.springframework.stereotype.Component;

/**
 * NEW REQUIREMENT
 * Add Slack notification.
 * We only add this class.
 * We do not modify NotificationService.
 */
@Component
public class SlackNotificationStrategy implements NotificationStrategy {

    @Override
    public String type() {
        return "SLACK";
    }

    @Override
    public String send(String message) {
        return "Slack sent: " + message;
    }
}