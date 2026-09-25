package com.pisethjava.template.lesson03_hook;

public abstract class ReportExporter {
	public final void export() {
		validate();
		loadData();
		format();
		compress();
		save();
	}

	private void validate() {
		System.out.println("Validate");
	}

	private void loadData() {
		System.out.println("Load data");
	}

	protected abstract void format();

	protected void compress() {
	}

	private void save() {
		System.out.println("Save");
	}
}
