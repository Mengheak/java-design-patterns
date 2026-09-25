package com.pisethjava.decorator.payment;

import java.util.Objects;

public final class AbaPaymentAdapter implements PaymentGateway {

    private final AbaSdk sdk;

    public AbaPaymentAdapter(AbaSdk sdk) {
        this.sdk = Objects.requireNonNull(sdk, "sdk is required");
    }

    @Override
    public PaymentResult pay(PaymentRequest request) {
        AbaResponse response = sdk.submit(
                request.account(),
                request.amount().doubleValue(),
                request.currency(),
                request.reference()
        );

        return new PaymentResult(
                response.transactionCode(),
                response.approved()
        );
    }
}
