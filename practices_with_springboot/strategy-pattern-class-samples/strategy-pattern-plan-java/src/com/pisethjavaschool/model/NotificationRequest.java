package com.pisethjavaschool.model;
public record NotificationRequest(
        String receiver,
        String message
) {
}