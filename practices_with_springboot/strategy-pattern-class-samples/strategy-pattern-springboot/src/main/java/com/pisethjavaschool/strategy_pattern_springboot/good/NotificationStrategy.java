package com.pisethjavaschool.strategy_pattern_springboot.good;
/**
 * Strategy interface.
 * Same goal: send notification.
 * Different behavior: email, sms, telegram, slack, etc.
 */
public interface NotificationStrategy {

    String type();

    String send(String message);
}
