package com.chheang.mengheak.factory.lesson02_simple;

import com.chheang.mengheak.factory.common.Report;

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
