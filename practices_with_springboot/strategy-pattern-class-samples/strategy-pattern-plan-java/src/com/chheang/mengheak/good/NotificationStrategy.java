package com.chheang.mengheak.good;

import com.chheang.mengheak.model.NotificationRequest;

public interface NotificationStrategy {

    NotificationChannel channel();

    String send(NotificationRequest request);
}