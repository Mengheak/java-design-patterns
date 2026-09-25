package com.chheang.mengheak.solid.s_srp.good;

import com.chheang.mengheak.solid.common.ReportService;
import com.chheang.mengheak.solid.common.User;

public class NewUserReportGenerator {

    private final ReportService reportService;

    public NewUserReportGenerator(ReportService reportService) {
        this.reportService = reportService;
    }

    public void generate(User user) {
        reportService.generateNewUserReport(user);
    }
}
