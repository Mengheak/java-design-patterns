package com.pisethjava.facade.order.payment;

import com.pisethjava.facade.order.domain.*;

public final class AbaPaymentGateway implements PaymentGateway {
    public boolean supports(String m) {
        return "ABA".equalsIgnoreCase(m);
    }
    public PaymentResult pay(OrderRequest r) {
        System.out.println("ABA adapter: process "+r.totalAmount());
        return new PaymentResult("ABA-"+r.orderNumber(),true);
    }
}
