package com.pisethjava.decorator.payment;

public final class MetricsPaymentDecorator extends PaymentGatewayDecorator {

    public MetricsPaymentDecorator(PaymentGateway delegate) {
        super(delegate);
    }

    @Override
    public PaymentResult pay(PaymentRequest request) {
        long start = System.nanoTime();

        try {
            return delegate.pay(request);
        } finally {
            System.out.println(
                    "[METRICS] payment.duration="
                            + (System.nanoTime() - start)
                            + "ns"
            );
        }
    }
}
