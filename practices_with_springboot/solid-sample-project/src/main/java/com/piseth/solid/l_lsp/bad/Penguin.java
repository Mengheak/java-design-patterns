package com.piseth.solid.l_lsp.bad;

/**
 * BAD EXAMPLE: Penguin is a Bird, but it cannot fly.
 */
public class Penguin extends Bird {

    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguin cannot fly");
    }
}
