package com.pisethjava.chain.demo;

import com.pisethjava.chain.expense.*;

import java.math.BigDecimal;

public final class ExpenseApprovalDemo {

    private ExpenseApprovalDemo() {
    }

    public static void run() {
        System.out.println("\n=== FIRST MATCHING HANDLER ===");

        ExpenseApprover teamLead = new TeamLeadApprover();
        ExpenseApprover manager = new ManagerApprover();
        ExpenseApprover director = new DirectorApprover();
        ExpenseApprover ceo = new CeoApprover();

        teamLead.setNext(manager).setNext(director).setNext(ceo);

        teamLead.approve(new ExpenseRequest("Laptop purchase", new BigDecimal("1500")));
        teamLead.approve(new ExpenseRequest("Office renovation", new BigDecimal("7000")));
    }
}
