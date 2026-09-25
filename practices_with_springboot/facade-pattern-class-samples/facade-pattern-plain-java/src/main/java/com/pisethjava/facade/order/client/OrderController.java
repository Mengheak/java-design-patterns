package com.pisethjava.facade.order.client;

import com.pisethjava.facade.order.domain.*;
import com.pisethjava.facade.order.facade.*;

public final class OrderController {
    private final OrderFacade facade;
    public OrderController(OrderFacade f) {
        facade=f;
    }
    public PlaceOrderResult placeOrder(OrderRequest r) {
        return facade.placeOrder(r);
    }
}
