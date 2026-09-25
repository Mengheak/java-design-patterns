package com.pisethjava.factory.lesson08_abstractfactory;

public class AbstractFactoryDemo {
    public static void main(String[] args) {
        LoginPage webPage = new LoginPage(new WebUIFactory());
        webPage.render();
        LoginPage mobilePage = new LoginPage(new MobileUIFactory());
        mobilePage.render();
    }
}
