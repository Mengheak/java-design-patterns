package com.pisethjavaschool.strategy_pattern_springboot.bad;

import org.springframework.stereotype.Service;

/**
 * BAD EXAMPLE
 * Problem:
 * - Too many if-else branches
 * - Violates Open/Closed Principle
 * - Every new notification type requires modifying this class
 */
@Service
public class BadNotificationService {

    public String send(String type, String message) {
        if ("EMAIL".equalsIgnoreCase(type)) {
            return "Email sent: " + message;
        } else if ("SMS".equalsIgnoreCase(type)) {
            return "SMS sent: " + message;
        } else if ("TELEGRAM".equalsIgnoreCase(type)) {
            return "Telegram sent: " + message;
        }else if ("SLACK".equalsIgnoreCase(type)) {
            return "SLACK sent: " + message;
        }

        throw new IllegalArgumentException("Unsupported notification type: " + type);
    }
}