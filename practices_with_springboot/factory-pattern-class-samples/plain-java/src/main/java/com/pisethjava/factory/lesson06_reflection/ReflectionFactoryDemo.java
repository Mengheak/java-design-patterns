package com.pisethjava.factory.lesson06_reflection;

import com.pisethjava.factory.common.Report;

public class ReflectionFactoryDemo {
    public static void main(String[] args) {
        ReflectionReportFactory factory = new ReflectionReportFactory();
        Report report = factory.create("com.pisethjava.factory.common.SalesReport");
        report.generate();
    }
}
