package com.pisethjava.facade.order.subsystem;

import com.pisethjava.facade.order.domain.*;

public final class InvoiceService {
    public Invoice generateInvoice(OrderRequest r) {
        var i=new Invoice("INV-"+r.orderNumber());
        System.out.println("7. Generate invoice");
        return i;
    }
}
