package com.piseth.solid.i_isp.bad;

/**
 * BAD EXAMPLE: Too many methods. Some classes are forced to implement methods they do not need.
 */
public interface Worker {

    void work();

    void eat();

    void drive();
}
