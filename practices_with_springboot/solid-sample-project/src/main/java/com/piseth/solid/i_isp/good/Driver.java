package com.piseth.solid.i_isp.good;

public class Driver implements Workable, Drivable {

    @Override
    public void work() {
        System.out.println("Driver delivers items");
    }

    @Override
    public void drive() {
        System.out.println("Driver drives a car");
    }
}
