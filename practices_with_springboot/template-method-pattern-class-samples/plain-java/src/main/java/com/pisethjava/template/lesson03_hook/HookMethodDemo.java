package com.pisethjava.template.lesson03_hook;

public class HookMethodDemo {
	public static void main(String[] args) {
		System.out.println("=== Normal PDF, no hook override ===");
		new PdfExporter().export();
		System.out.println("=== Compressed PDF, hook override ===");
		new CompressedPdfExporter().export();
	}
}
