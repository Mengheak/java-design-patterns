package com.piseth.solid.i_isp.good;

public class Developer implements Workable, Eatable {

    @Override
    public void work() {
        System.out.println("Developer writes code");
    }

    @Override
    public void eat() {
        System.out.println("Developer eats lunch");
    }
}
