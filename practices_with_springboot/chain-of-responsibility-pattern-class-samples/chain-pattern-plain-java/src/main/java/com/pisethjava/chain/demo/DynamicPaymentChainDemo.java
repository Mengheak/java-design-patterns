package com.pisethjava.chain.demo;

import com.pisethjava.chain.payment.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class DynamicPaymentChainDemo {

    private DynamicPaymentChainDemo() {
    }

    public static void run() {
        System.out.println("\n=== DYNAMIC CHAIN ===");

        boolean fraudCheckEnabled = false;
        List<PaymentHandler> handlers = new ArrayList<>();

        handlers.add(new ValidationHandler());
        handlers.add(new AuthenticationHandler());
        handlers.add(new AuthorizationHandler());

        if (fraudCheckEnabled) {
            handlers.add(new FraudHandler());
        }

        handlers.add(new ProcessPaymentHandler());

        PaymentHandler head = link(handlers);
        head.handle(new PaymentContext(
                new PaymentRequest(new BigDecimal("99.00"), "USD"),
                "USER-3",
                Set.of("PAYMENT_CREATE"),
                2
        ));
    }

    private static PaymentHandler link(List<PaymentHandler> handlers) {
        for (int i = 0; i < handlers.size() - 1; i++) {
            handlers.get(i).setNext(handlers.get(i + 1));
        }

        return handlers.get(0);
    }
}
