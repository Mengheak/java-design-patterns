package com.pisethjava.template.lesson02_template;

public class TemplateMethodDemo {
	public static void main(String[] args) {
		System.out.println("=== GOOD CODE: Template Method ===");
		ReportExporter exporter = new ExcelReportExporter();
		exporter.export();
		/*
		System.out.println();
		exporter = new ExcelReportExporter();
		exporter.export();
		System.out.println();
		exporter = new JsonReportExporter();
		exporter.export();
		*/
	}
}
