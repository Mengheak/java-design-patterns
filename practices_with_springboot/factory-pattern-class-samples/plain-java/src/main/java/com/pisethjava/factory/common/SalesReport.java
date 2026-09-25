package com.pisethjava.factory.common;

public class SalesReport implements Report {
    @Override
    public void generate() {
        System.out.println("Generating SALES report");
    }
}
