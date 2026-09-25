package com.piseth.solid.o_ocp.good;

import java.math.BigDecimal;
import java.util.List;

/**
 * GOOD EXAMPLE: Add a new policy class without changing this calculator.
 */
public class DiscountCalculator {

    private final List<DiscountPolicy> policies;

    public DiscountCalculator(List<DiscountPolicy> policies) {
        this.policies = policies;
    }

    public BigDecimal calculate(String membership, BigDecimal amount) {
        return policies.stream()
                .filter(policy -> policy.supports(membership))
                .findFirst()
                .map(policy -> policy.calculate(amount))
                .orElse(BigDecimal.ZERO);
    }
}
