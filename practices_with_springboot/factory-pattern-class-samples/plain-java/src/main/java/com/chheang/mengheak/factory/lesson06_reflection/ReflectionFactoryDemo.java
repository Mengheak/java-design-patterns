package com.chheang.mengheak.factory.lesson06_reflection;

import com.chheang.mengheak.factory.common.Report;

public class ReflectionFactoryDemo {
    public static void main(String[] args) {
        ReflectionReportFactory factory = new ReflectionReportFactory();
        Report report = factory.create("com.chheang.mengheak.factory.common.SalesReport");
        report.generate();
    }
}
