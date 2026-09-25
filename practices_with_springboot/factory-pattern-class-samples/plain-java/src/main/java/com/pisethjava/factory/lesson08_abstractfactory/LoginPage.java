package com.pisethjava.factory.lesson08_abstractfactory;

public class LoginPage {
    private final Button button;
    private final Input input;
    public LoginPage(UIFactory factory) {
        this.button = factory.createButton();
        this.input = factory.createInput();
    }
    public void render() {
        input.render();
        button.render();
    }
}
