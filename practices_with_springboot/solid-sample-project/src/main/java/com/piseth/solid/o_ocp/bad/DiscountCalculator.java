package com.piseth.solid.o_ocp.bad;

import java.math.BigDecimal;

/**
 * BAD EXAMPLE: Every new membership requires changing this class.
 */
public class DiscountCalculator {

    public BigDecimal calculate(String membership, BigDecimal amount) {
        if ("SILVER".equals(membership)) {
            return amount.multiply(BigDecimal.valueOf(0.05));
        }

        if ("GOLD".equals(membership)) {
            return amount.multiply(BigDecimal.valueOf(0.10));
        }

        if ("PLATINUM".equals(membership)) {
            return amount.multiply(BigDecimal.valueOf(0.15));
        }

        return BigDecimal.ZERO;
    }
}
