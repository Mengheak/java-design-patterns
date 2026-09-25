package com.pisethjava.factory.lesson01_problem;

import com.pisethjava.factory.common.*;

public class BadReportService {
    public void generate(String type) {
        Report report;
        if ("SALES".equalsIgnoreCase(type)) {
            report = new SalesReport();
        } else if ("STOCK".equalsIgnoreCase(type)) {
            report = new StockReport();
        } else if ("CUSTOMER".equalsIgnoreCase(type)) {
            report = new CustomerReport();
        } else {
            throw new IllegalArgumentException("Unsupported report type: " + type);
        }
        report.generate();
    }
}
