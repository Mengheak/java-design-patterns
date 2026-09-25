package com.pisethjava.factory.lesson01_problem;

public class ProblemDemo {
    public static void main(String[] args) {
        BadReportService service = new BadReportService();
        //service.generate("SALES");
        service.generate("STOCK");
    }
}
