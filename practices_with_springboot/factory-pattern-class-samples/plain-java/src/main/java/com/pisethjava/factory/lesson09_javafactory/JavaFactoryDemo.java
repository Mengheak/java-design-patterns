package com.pisethjava.factory.lesson09_javafactory;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaFactoryDemo {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println("Calendar implementation: " + calendar.getClass().getName());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        System.out.println(formatter.format(99.99));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> System.out.println("Executor created by factory"));
        executor.shutdown();
    }
}
