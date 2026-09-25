package com.pisethjava.chain.expense;

public abstract class ExpenseApprover {

    private ExpenseApprover next;

    public ExpenseApprover setNext(ExpenseApprover next) {
        this.next = next;
        return next;
    }

    public void approve(ExpenseRequest request) {
        if (canApprove(request)) {
            doApprove(request);
            return;
        }

        if (next != null) {
            next.approve(request);
            return;
        }

        throw new IllegalStateException("No approver available for amount: " + request.amount());
    }

    protected abstract boolean canApprove(ExpenseRequest request);
    protected abstract void doApprove(ExpenseRequest request);
}
