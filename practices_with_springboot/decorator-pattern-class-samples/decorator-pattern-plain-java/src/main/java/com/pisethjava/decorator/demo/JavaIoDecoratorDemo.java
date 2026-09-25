package com.pisethjava.decorator.demo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class JavaIoDecoratorDemo {

    private JavaIoDecoratorDemo() {
    }

    public static void run() throws Exception {
        System.out.println("\n=== 5. JAVA I/O DECORATOR ===");

        byte[] bytes = "Java Decorator".getBytes(StandardCharsets.UTF_8);

        InputStream stream =
                new DataInputStream(
                        new BufferedInputStream(
                                new ByteArrayInputStream(bytes)
                        )
                );

        System.out.println("Read first byte through decorated InputStream: " + stream.read());
    }
}
