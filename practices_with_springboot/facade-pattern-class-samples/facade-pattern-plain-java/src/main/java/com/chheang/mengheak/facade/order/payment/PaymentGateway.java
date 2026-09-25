package com.chheang.mengheak.facade.order.payment;

import com.chheang.mengheak.facade.order.domain.*;

public interface PaymentGateway {
    boolean supports(String method);
    PaymentResult pay(OrderRequest request);
}
