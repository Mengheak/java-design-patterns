package com.pisethjava.template.lesson02_template;

public class PdfReportExporter extends ReportExporter {
	@Override
	protected void formatData() {
		System.out.println("Format data as Pdf");
	}

	@Override
	protected void generateFile() {
		System.out.println("Generate Pdf file");
	}
}
