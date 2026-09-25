package com.chheang.mengheak.facade.order.subsystem;

import com.chheang.mengheak.facade.order.domain.*;

public final class AuditService {
    public void recordOrderCreated(OrderRequest r) {
        System.out.println("9. Audit "+r.orderNumber());
    }
}
