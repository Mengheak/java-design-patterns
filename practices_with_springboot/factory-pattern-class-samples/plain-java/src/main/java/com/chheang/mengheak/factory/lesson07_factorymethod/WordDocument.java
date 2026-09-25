package com.chheang.mengheak.factory.lesson07_factorymethod;

public class WordDocument implements Document {
	@Override
	public void open() {
		System.out.println("Open Word document");
	}
}
