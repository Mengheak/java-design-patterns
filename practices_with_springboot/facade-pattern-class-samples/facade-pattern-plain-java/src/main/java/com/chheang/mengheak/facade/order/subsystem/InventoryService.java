package com.chheang.mengheak.facade.order.subsystem;

import com.chheang.mengheak.facade.order.domain.*;

public final class InventoryService {
    public void checkAvailability(OrderRequest r) {
        System.out.println("3. Check inventory");
    }
    public void reserve(OrderRequest r) {
        System.out.println("4. Reserve products");
    }
}
