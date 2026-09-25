package com.pisethjava.template.lesson03_hook;

public class PdfExporter extends ReportExporter {
	@Override
	protected void format() {
		System.out.println("Format PDF");
	}
}
