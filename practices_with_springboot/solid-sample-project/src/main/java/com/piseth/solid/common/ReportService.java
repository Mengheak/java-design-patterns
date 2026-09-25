package com.piseth.solid.common;

public class ReportService {

    public void generateNewUserReport(User user) {
        System.out.println("Generating report for user: " + user.getEmail());
    }
}
