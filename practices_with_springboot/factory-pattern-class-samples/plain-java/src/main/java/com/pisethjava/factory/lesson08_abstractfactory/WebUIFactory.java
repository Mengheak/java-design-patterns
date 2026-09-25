package com.pisethjava.factory.lesson08_abstractfactory;

public class WebUIFactory implements UIFactory {
	@Override
	public Button createButton() {
		return new WebButton();
	}

	@Override
	public Input createInput() {
		return new WebInput();
	}
}
