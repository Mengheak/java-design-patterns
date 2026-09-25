package com.pisethjava.factory.lesson02_simple;

import com.pisethjava.factory.common.Report;

public class ReportService {
	private final ReportFactory factory;

	public ReportService(ReportFactory factory) {
		this.factory = factory;
	}

	public void generate(String type) {
		Report report = factory.create(type);
		report.generate();
	}
}
