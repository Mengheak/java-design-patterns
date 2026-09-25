package com.piseth.solid.s_srp.good;

import com.piseth.solid.common.ReportService;
import com.piseth.solid.common.User;

public class NewUserReportGenerator {

    private final ReportService reportService;

    public NewUserReportGenerator(ReportService reportService) {
        this.reportService = reportService;
    }

    public void generate(User user) {
        reportService.generateNewUserReport(user);
    }
}
