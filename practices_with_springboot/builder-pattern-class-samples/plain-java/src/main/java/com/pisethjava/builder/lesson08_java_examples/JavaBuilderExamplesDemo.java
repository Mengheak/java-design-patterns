package com.pisethjava.builder.lesson08_java_examples;

import java.net.URI;
import java.net.http.HttpRequest;

public class JavaBuilderExamplesDemo {
    public static void main(String[] args) {
        String message = new StringBuilder()
                .append("Hello ")
                .append("Builder ")
                .append("Pattern")
                .toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://example.com"))
                .GET()
                .build();

        ProcessBuilder processBuilder = new ProcessBuilder("java", "-version");

        System.out.println(message);
        System.out.println(request.method() + " " + request.uri());
        System.out.println(processBuilder.command());
    }
}








