package com.pisethjava.factory.common;

public class CustomerReport implements Report {
    @Override
    public void generate() {
        System.out.println("Generating CUSTOMER report");
    }
}
