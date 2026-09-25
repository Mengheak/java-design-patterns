package com.pisethjava.decorator.payment;

public final class AbaSdk {

    private int callCount;

    public AbaResponse submit(
            String account,
            double amount,
            String currency,
            String reference
    ) {
        callCount++;

        if (reference.startsWith("RETRY") && callCount < 3) {
            throw new IllegalStateException("Temporary ABA failure");
        }

        return new AbaResponse(
                "ABA-" + reference,
                true
        );
    }
}
