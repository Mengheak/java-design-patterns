package com.pisethjava.factory.lesson02_simple;

import com.pisethjava.factory.common.*;

public class ReportFactory {
	public Report create(String type) {
		if ("SALES".equalsIgnoreCase(type))
			return new SalesReport();
		if ("STOCK".equalsIgnoreCase(type))
			return new StockReport();
		if ("CUSTOMER".equalsIgnoreCase(type))
			return new CustomerReport();
		throw new IllegalArgumentException("Unsupported report type: " + type);
	}
}
