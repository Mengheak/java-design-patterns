package com.pisethjava.chain.before;

import java.util.Set;

public final class PaymentContext {

    private final PaymentRequest request;
    private final String userId;
    private final Set<String> permissions;
    private final int requestsToday;

    private boolean processed;

    public PaymentContext(
            PaymentRequest request,
            String userId,
            Set<String> permissions,
            int requestsToday
    ) {
        this.request = request;
        this.userId = userId;
        this.permissions = permissions;
        this.requestsToday = requestsToday;
    }

    public PaymentRequest request() {
        return request;
    }

    public String userId() {
        return userId;
    }

    public Set<String> permissions() {
        return permissions;
    }

    public int requestsToday() {
        return requestsToday;
    }

    public boolean processed() {
        return processed;
    }

    public void markProcessed() {
        this.processed = true;
    }
}
