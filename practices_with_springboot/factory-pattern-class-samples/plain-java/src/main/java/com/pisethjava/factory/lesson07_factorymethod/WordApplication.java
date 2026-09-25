package com.pisethjava.factory.lesson07_factorymethod;

public class WordApplication extends DocumentApplication {
	@Override
	protected Document createDocument() {
		return new WordDocument();
	}
}
