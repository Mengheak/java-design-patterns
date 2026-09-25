package com.pisethjava.factory.lesson08_abstractfactory;

public class MobileButton implements Button {
	@Override
	public void render() {
		System.out.println("Render mobile button");
	}
}
