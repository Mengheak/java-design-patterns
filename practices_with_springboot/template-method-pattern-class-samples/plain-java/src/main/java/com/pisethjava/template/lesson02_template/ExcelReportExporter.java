package com.pisethjava.template.lesson02_template;

public class ExcelReportExporter extends ReportExporter {
	@Override
	protected void formatData() {
		System.out.println("Format data as Excel");
	}

	@Override
	protected void generateFile() {
		System.out.println("Generate Excel file");
	}
}
