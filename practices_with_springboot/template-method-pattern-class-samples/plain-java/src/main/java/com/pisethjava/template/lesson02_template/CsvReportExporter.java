package com.pisethjava.template.lesson02_template;

public class CsvReportExporter extends ReportExporter {
	@Override
	protected void formatData() {
		System.out.println("Format data as Csv");
	}

	@Override
	protected void generateFile() {
		System.out.println("Generate Csv file");
	}
}
