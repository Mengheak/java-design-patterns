package com.pisethjava.factory.lesson07_factorymethod;

public class WordDocument implements Document {
	@Override
	public void open() {
		System.out.println("Open Word document");
	}
}
