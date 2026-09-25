package com.pisethjava.template.lesson01_problem;

public class DuplicateWorkflowDemo {
	public static void main(String[] args) {
		System.out.println("=== BAD CODE: Duplicate workflow ===");
		new PdfReportExporter().export();
		System.out.println();
		new ExcelReportExporter().export();
		System.out.println();
		new CsvReportExporter().export();
	}
}
