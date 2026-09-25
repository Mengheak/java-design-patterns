package com.pisethjava.facade.order.subsystem;

import java.util.*;
import com.pisethjava.facade.order.domain.*;
import com.pisethjava.facade.order.payment.*;

public final class PaymentService {
    private final List<PaymentGateway> gateways;
    public PaymentService(List<PaymentGateway> g) {
        gateways=List.copyOf(g);
    }
    public PaymentResult processPayment(OrderRequest r) {
        var g=gateways.stream().filter(x->x.supports(r.paymentMethod())).findFirst().orElseThrow();
        var result=g.pay(r);
        System.out.println("5. Payment accepted");
        return result;
    }
}
