package com.pisethjava.factory.lesson08_abstractfactory;

public class WebButton implements Button {
	@Override
	public void render() {
		System.out.println("Render web button");
	}
}
