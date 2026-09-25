package com.pisethjava.facade.order.subsystem;

import com.pisethjava.facade.order.domain.*;

public final class NotificationService {
    public void sendConfirmation(String c,Invoice i) {
        System.out.println("8. Notify "+c);
    }
}
