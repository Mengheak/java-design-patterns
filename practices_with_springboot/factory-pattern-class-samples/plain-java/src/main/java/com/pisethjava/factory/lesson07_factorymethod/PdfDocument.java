package com.pisethjava.factory.lesson07_factorymethod;

public class PdfDocument implements Document {
	@Override
	public void open() {
		System.out.println("Open PDF document");
	}
}
