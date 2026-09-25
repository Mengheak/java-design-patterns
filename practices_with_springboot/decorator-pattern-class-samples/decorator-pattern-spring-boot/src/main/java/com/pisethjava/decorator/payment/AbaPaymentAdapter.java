package com.pisethjava.decorator.payment;

public final class AbaPaymentAdapter implements PaymentGateway {

    private final AbaSdk sdk;

    public AbaPaymentAdapter(AbaSdk sdk) {
        this.sdk = sdk;
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
                response.approved(),
                "ABA"
        );
    }
}
