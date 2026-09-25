package com.pisethjava.factory.lesson08_abstractfactory;

public class MobileUIFactory implements UIFactory {
	@Override
	public Button createButton() {
		return new MobileButton();
	}

	@Override
	public Input createInput() {
		return new MobileInput();
	}
}
