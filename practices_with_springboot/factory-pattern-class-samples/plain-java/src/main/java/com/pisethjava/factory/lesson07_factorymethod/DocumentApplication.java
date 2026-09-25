package com.pisethjava.factory.lesson07_factorymethod;

public abstract class DocumentApplication {
	public void processDocument() {
		Document document = createDocument();
		document.open();
	}

	protected abstract Document createDocument();
}
