package com.pisethjava.facade.order.subsystem;

import com.pisethjava.facade.order.domain.*;

public final class AuditService {
    public void recordOrderCreated(OrderRequest r) {
        System.out.println("9. Audit "+r.orderNumber());
    }
}
