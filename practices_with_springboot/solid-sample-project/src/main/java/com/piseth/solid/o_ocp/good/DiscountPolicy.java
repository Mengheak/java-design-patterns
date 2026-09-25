package com.piseth.solid.o_ocp.good;

import java.math.BigDecimal;

public interface DiscountPolicy {

    boolean supports(String membership);

    BigDecimal calculate(BigDecimal amount);
}
