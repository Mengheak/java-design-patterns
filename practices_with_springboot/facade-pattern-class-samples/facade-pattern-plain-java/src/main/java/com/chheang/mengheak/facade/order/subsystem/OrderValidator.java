package com.chheang.mengheak.facade.order.subsystem;

import com.chheang.mengheak.facade.order.domain.*;

public final class OrderValidator {
    public void validate(OrderRequest r) {
        if(r.productIds()==null||r.productIds().isEmpty())throw new IllegalArgumentException("Products required");
        System.out.println("2. Validate order: "+r.orderNumber());
    }
}
