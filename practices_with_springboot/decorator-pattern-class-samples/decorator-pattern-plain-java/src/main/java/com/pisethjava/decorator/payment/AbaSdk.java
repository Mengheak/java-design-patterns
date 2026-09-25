package com.pisethjava.decorator.payment;

public final class AbaSdk {

    public AbaResponse submit(
            String account,
            double amount,
            String currency,
            String reference
    ) {
        System.out.printf(
                "ABA SDK: account=%s, amount=%.2f %s, reference=%s%n",
                account,
                amount,
                currency,
                reference
        );

        return new AbaResponse(
                "ABA-" + reference,
                true
        );
    }
}
