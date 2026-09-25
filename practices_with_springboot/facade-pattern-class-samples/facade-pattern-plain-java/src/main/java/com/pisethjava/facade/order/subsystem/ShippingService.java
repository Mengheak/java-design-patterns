package com.pisethjava.facade.order.subsystem;

import com.pisethjava.facade.order.domain.*;

public final class ShippingService {
    public Shipment createShipment(OrderRequest r) {
        var s=new Shipment("SHIP-"+r.orderNumber());
        System.out.println("6. Create shipment");
        return s;
    }
}
