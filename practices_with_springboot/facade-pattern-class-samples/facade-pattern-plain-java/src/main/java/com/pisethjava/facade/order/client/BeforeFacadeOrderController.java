package com.pisethjava.facade.order.client;

import com.pisethjava.facade.order.domain.*;
import com.pisethjava.facade.order.subsystem.*;

public final class BeforeFacadeOrderController {
    private final CustomerService c;
    private final OrderValidator v;
    private final InventoryService i;
    private final PaymentService p;
    private final ShippingService s;
    private final InvoiceService n;
    private final NotificationService notify;
    private final AuditService a;
    public BeforeFacadeOrderController(CustomerService c,OrderValidator v,InventoryService i,PaymentService p,ShippingService s,InvoiceService n,NotificationService notify,AuditService a) {
        this.c=c;
        this.v=v;
        this.i=i;
        this.p=p;
        this.s=s;
        this.n=n;
        this.notify=notify;
        this.a=a;
    }
    public PlaceOrderResult placeOrder(OrderRequest r) {
        c.validateCustomer(r.customerId());
        v.validate(r);
        i.checkAvailability(r);
        i.reserve(r);
        var pay=p.processPayment(r);
        var ship=s.createShipment(r);
        var inv=n.generateInvoice(r);
        notify.sendConfirmation(r.customerId(),inv);
        a.recordOrderCreated(r);
        return new PlaceOrderResult(r.orderNumber(),pay.transactionId(),ship.shipmentNumber(),inv.invoiceNumber(),"ORDER_CREATED");
    }
}
