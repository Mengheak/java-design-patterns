package com.pisethjava.decorator.storage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class S3Client {

    private final Map<String, byte[]> objects = new HashMap<>();

    public void putObject(String bucket, String key, byte[] content) {
        objects.put(bucket + "/" + key, Arrays.copyOf(content, content.length));
    }

    public byte[] getObject(String bucket, String key) {
        byte[] content = objects.get(bucket + "/" + key);

        if (content == null) {
            throw new IllegalArgumentException("Object not found: " + key);
        }

        return Arrays.copyOf(content, content.length);
    }
}
