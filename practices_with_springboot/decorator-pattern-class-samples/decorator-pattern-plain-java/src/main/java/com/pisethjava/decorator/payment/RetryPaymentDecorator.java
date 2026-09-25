package com.pisethjava.decorator.payment;

public final class RetryPaymentDecorator extends PaymentGatewayDecorator {

    private final int maxAttempts;

    public RetryPaymentDecorator(
            PaymentGateway delegate,
            int maxAttempts
    ) {
        super(delegate);
        this.maxAttempts = maxAttempts;
    }

    @Override
    public PaymentResult pay(PaymentRequest request) {
        RuntimeException lastFailure = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("[RETRY] Payment attempt " + attempt);
                return delegate.pay(request);
            } catch (RuntimeException exception) {
                lastFailure = exception;
            }
        }

        throw lastFailure;
    }
}
