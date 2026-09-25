package com.chheang.mengheak.factory.lesson07_factorymethod;

public class FactoryMethodDemo {
    public static void main(String[] args) {
        DocumentApplication pdfApp = new PdfApplication();
        pdfApp.processDocument();
        DocumentApplication wordApp = new WordApplication();
        wordApp.processDocument();
    }
}
