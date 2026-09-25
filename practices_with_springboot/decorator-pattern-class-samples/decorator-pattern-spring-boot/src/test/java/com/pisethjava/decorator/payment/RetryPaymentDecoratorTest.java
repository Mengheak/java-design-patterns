package com.pisethjava.decorator.payment;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class RetryPaymentDecoratorTest {

    @Test
    void shouldRetryUntilSuccess() {
        AtomicInteger attempts = new AtomicInteger();

        PaymentGateway unstable = request -> {
            if (attempts.incrementAndGet() < 3) {
                throw new IllegalStateException("Temporary failure");
            }

            return new PaymentResult("TXN-1", true, "FAKE");
        };

        PaymentGateway gateway = new RetryPaymentDecorator(unstable, 3);

        PaymentResult result = gateway.pay(
                new PaymentRequest(
                        "account",
                        new BigDecimal("10.00"),
                        "USD",
                        "ORDER-1"
                )
        );

        assertThat(result.successful()).isTrue();
        assertThat(attempts).hasValue(3);
    }
}
