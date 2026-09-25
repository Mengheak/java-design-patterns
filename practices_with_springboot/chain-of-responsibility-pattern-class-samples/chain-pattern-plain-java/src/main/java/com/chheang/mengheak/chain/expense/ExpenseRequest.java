package com.chheang.mengheak.chain.expense;

import java.math.BigDecimal;

public record ExpenseRequest(String description, BigDecimal amount) {
}
