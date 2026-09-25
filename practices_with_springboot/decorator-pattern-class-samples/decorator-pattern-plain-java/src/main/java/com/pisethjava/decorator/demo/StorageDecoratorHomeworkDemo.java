package com.pisethjava.decorator.demo;

import com.pisethjava.decorator.storage.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class StorageDecoratorHomeworkDemo {

    private StorageDecoratorHomeworkDemo() {
    }

    public static void run() {
        System.out.println("\n=== 7. STORAGE HOMEWORK ANSWER ===");

        byte[] original = (
                "Room Cambodia Decorator Pattern Demo. "
                        + "This repeated sentence improves compression. "
                        + "Room Cambodia Decorator Pattern Demo. "
                        + "This repeated sentence improves compression."
        ).getBytes(StandardCharsets.UTF_8);

        StorageService localStorage =
                new MetricsStorageDecorator(
                        new LoggingStorageDecorator(
                                new EncryptionStorageDecorator(
                                        new CompressionStorageDecorator(
                                                new LocalStorageService()
                                        )
                                )
                        )
                );

        localStorage.upload("lesson.txt", original);
        byte[] downloaded = localStorage.download("lesson.txt");

        System.out.println("Local content restored: " + Arrays.equals(original, downloaded));
        System.out.println(new String(downloaded, StandardCharsets.UTF_8));

        System.out.println("\nDecorating an Adapter:");

        StorageService s3Storage =
                new LoggingStorageDecorator(
                        new EncryptionStorageDecorator(
                                new S3StorageAdapter(
                                        new S3Client(),
                                        "course-files"
                                )
                        )
                );

        s3Storage.upload("s3-demo.txt", original);
        byte[] s3Downloaded = s3Storage.download("s3-demo.txt");
        System.out.println("S3 content restored: " + Arrays.equals(original, s3Downloaded));
    }
}
