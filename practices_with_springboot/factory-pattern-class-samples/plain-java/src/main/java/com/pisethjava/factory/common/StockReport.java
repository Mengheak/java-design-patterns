package com.pisethjava.factory.common;

public class StockReport implements Report {
    @Override
    public void generate() {
        System.out.println("Generating STOCK report");
    }
}
