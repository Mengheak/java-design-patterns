package com.pisethjava.factory.lesson05_registry;

public class RegistryFactoryDemo {
    public static void main(String[] args) {
        RegistryReportFactory factory = new RegistryReportFactory();
        factory.create("SALES").generate();
        factory.create("CUSTOMER").generate();
    }
}
