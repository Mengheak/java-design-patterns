package com.pisethjava.factory.lesson02_simple;

public class SimpleFactoryDemo {
    public static void main(String[] args) {
        ReportService service = new ReportService(new ReportFactory());
        service.generate("SALES");
        //service.generate("CUSTOMER");
    }
}
