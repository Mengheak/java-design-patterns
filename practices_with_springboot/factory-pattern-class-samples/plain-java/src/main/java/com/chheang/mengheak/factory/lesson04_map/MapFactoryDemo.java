package com.chheang.mengheak.factory.lesson04_map;

public class MapFactoryDemo {
    public static void main(String[] args) {
        MapBasedReportFactory factory = new MapBasedReportFactory();
        factory.getReport("SALES").generate();
        //factory.getReport("STOCK").generate();
    }
}
