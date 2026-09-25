package com.pisethjava.factory.lesson04_map;

import com.pisethjava.factory.common.*;
import java.util.Map;

public class MapBasedReportFactory {
    private final Map<String, Report> reports = Map.of(
            "SALES", new SalesReport(),
            "STOCK", new StockReport(),
            "CUSTOMER", new CustomerReport()
    );

    public Report getReport(String type) {
        Report report = reports.get(type.toUpperCase());
        if (report == null) throw new IllegalArgumentException("Unsupported report type: " + type);
        return report;
    }
}
