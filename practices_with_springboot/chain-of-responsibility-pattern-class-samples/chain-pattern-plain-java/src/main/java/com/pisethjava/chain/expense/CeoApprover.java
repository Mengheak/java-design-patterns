package com.pisethjava.chain.expense;

import java.math.BigDecimal;

public final class CeoApprover extends ExpenseApprover {

    private static final BigDecimal LIMIT = new BigDecimal("999999999");

    @Override
    protected boolean canApprove(ExpenseRequest request) {
        return request.amount().compareTo(LIMIT) <= 0;
    }

    @Override
    protected void doApprove(ExpenseRequest request) {
        System.out.println("Approved by CEO: " + request.description() + " -> " + request.amount());
    }
}
