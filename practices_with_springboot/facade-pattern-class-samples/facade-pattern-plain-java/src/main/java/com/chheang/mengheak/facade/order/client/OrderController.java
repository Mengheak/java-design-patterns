package com.chheang.mengheak.facade.order.client;

import com.chheang.mengheak.facade.order.domain.*;
import com.chheang.mengheak.facade.order.facade.*;

public final class OrderController {
    private final OrderFacade facade;
    public OrderController(OrderFacade f) {
        facade=f;
    }
    public PlaceOrderResult placeOrder(OrderRequest r) {
        return facade.placeOrder(r);
    }
}
