package com.chheang.mengheak.factory.common;

public class StockReport implements Report {
    @Override
    public void generate() {
        System.out.println("Generating STOCK report");
    }
}
