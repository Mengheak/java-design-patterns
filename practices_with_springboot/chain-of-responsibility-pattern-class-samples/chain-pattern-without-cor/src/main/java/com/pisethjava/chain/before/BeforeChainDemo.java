package com.pisethjava.chain.before;

import java.math.BigDecimal;
import java.util.Set;

public final class BeforeChainDemo {

    private BeforeChainDemo() {
    }

    public static void main(String[] args) {
        successfulPayment();
        failedAuthorization();
        growingRequirements();
    }

    private static void successfulPayment() {
        System.out.println(
                "\n=== WITHOUT CHAIN: SUCCESS ==="
        );

        PaymentService paymentService =
                new PaymentService();

        PaymentContext context =
                new PaymentContext(
                        new PaymentRequest(
                                new BigDecimal("150.00"),
                                "USD"
                        ),
                        "USER-1",
                        Set.of("PAYMENT_CREATE"),
                        10
                );

        paymentService.pay(context);

        System.out.println(
                "Processed = "
                        + context.processed()
        );
    }

    private static void failedAuthorization() {
        System.out.println(
                "\n=== WITHOUT CHAIN: FAILURE ==="
        );

        PaymentService paymentService =
                new PaymentService();

        PaymentContext context =
                new PaymentContext(
                        new PaymentRequest(
                                new BigDecimal("50.00"),
                                "USD"
                        ),
                        "USER-2",
                        Set.of(),
                        1
                );

        try {
            paymentService.pay(context);
        } catch (RuntimeException exception) {
            System.out.println(
                    "Payment stopped: "
                            + exception.getMessage()
            );
        }

        System.out.println(
                "Processed = "
                        + context.processed()
        );
    }

    private static void growingRequirements() {
        System.out.println(
                "\n=== SAME SERVICE AFTER MORE REQUIREMENTS ==="
        );

        PaymentServiceWithMoreRequirements
                paymentService =
                new PaymentServiceWithMoreRequirements();

        PaymentContext context =
                new PaymentContext(
                        new PaymentRequest(
                                new BigDecimal("500.00"),
                                "USD"
                        ),
                        "USER-3",
                        Set.of("PAYMENT_CREATE"),
                        5
                );

        paymentService.pay(
                context,
                "KH",
                false,
                false
        );

        System.out.println(
                "Notice how every new requirement "
                        + "changed the same service."
        );
    }
}
