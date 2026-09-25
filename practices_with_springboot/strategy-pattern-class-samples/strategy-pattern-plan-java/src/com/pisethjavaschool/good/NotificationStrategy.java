package com.pisethjavaschool.good;

import com.pisethjavaschool.model.NotificationRequest;

public interface NotificationStrategy {

    NotificationChannel channel();

    String send(NotificationRequest request);
}