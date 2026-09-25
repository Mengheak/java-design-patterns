package com.pisethjava.decorator.demo;

import com.pisethjava.decorator.payment.*;

import java.math.BigDecimal;

public final class AdapterDecoratorDemo {

    private AdapterDecoratorDemo() {
    }

    public static void run() {
        System.out.println("\n=== 6. ADAPTER + DECORATOR ===");

        PaymentGateway gateway =
                new MetricsPaymentDecorator(
                        new RetryPaymentDecorator(
                                new LoggingPaymentDecorator(
                                        new AbaPaymentAdapter(
                                                new AbaSdk()
                                        )
                                ),
                                3
                        )
                );

        PaymentResult result = gateway.pay(
                new PaymentRequest(
                        "001-123456-9",
                        new BigDecimal("125.50"),
                        "USD",
                        "ORDER-1001"
                )
        );

        System.out.println("Result: " + result);
    }
}
