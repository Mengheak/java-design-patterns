package com.pisethjava.template.lesson05_java_examples;

import java.io.*;
import java.util.AbstractList;

public class JavaTemplateExamplesDemo {
	public static void main(String[] args) throws IOException {
		System.out.println("=== InputStream example ===");
		InputStream inputStream = new ByteArrayInputStream("ABC".getBytes());
		System.out.println("Read first byte: " + (char) inputStream.read());
		System.out.println("=== AbstractList example ===");
		AbstractList<String> list = new AbstractList<>() {
			private final String[] data = { "Java", "Spring", "Design Pattern" };

			public String get(int index) {
				return data[index];
			}

			public int size() {
				return data.length;
			}
		};
		System.out.println(list);
	}
}
