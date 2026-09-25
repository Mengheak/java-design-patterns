package com.pisethjava.facade.order.payment;

import com.pisethjava.facade.order.domain.*;

public interface PaymentGateway {
    boolean supports(String method);
    PaymentResult pay(OrderRequest request);
}
