package com.pisethjava.template.lesson03_hook;

public class CompressedPdfExporter extends ReportExporter {
	@Override
	protected void format() {
		System.out.println("Format PDF");
	}

	@Override
	protected void compress() {
		System.out.println("Compress PDF");
	}
}
