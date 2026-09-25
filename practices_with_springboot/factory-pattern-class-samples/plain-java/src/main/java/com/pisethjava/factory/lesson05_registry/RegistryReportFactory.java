package com.pisethjava.factory.lesson05_registry;

import com.pisethjava.factory.common.*;
import java.util.Map;
import java.util.function.Supplier;

public class RegistryReportFactory {
    private final Map<String, Supplier<Report>> registry = Map.of(
            "SALES", SalesReport::new,
            "STOCK", StockReport::new,
            "CUSTOMER", CustomerReport::new
    );

    public Report create(String type) {
        Supplier<Report> supplier = registry.get(type.toUpperCase());
        if (supplier == null) throw new IllegalArgumentException("Unsupported report type: " + type);
        return supplier.get();
    }
}
