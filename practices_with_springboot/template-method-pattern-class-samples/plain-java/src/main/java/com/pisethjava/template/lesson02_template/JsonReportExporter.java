package com.pisethjava.template.lesson02_template;

public class JsonReportExporter extends ReportExporter {
	@Override
	protected void formatData() {
		System.out.println("Format data as Json");
	}

	@Override
	protected void generateFile() {
		System.out.println("Generate Json file");
	}
}
