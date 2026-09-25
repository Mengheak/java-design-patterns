package com.chheang.mengheak.strategy_pattern_springboot.good;
public class UnsupportedNotificationTypeException extends RuntimeException {

    public UnsupportedNotificationTypeException(String type) {
        super("Unsupported notification type: " + type);
    }
}