package com.piseth.solid.i_isp.bad;

public class Developer implements Worker {

    @Override
    public void work() {
        System.out.println("Developer writes code");
    }

    @Override
    public void eat() {
        System.out.println("Developer eats lunch");
    }

    @Override
    public void drive() {
        throw new UnsupportedOperationException("Developer does not drive for this job");
    }
}
