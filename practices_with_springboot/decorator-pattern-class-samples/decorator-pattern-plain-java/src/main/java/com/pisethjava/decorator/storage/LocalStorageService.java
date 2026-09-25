package com.pisethjava.decorator.storage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class LocalStorageService implements StorageService {

    private final Map<String, byte[]> files = new HashMap<>();

    @Override
    public void upload(String filename, byte[] content) {
        files.put(filename, Arrays.copyOf(content, content.length));
    }

    @Override
    public byte[] download(String filename) {
        byte[] content = files.get(filename);

        if (content == null) {
            throw new IllegalArgumentException("File not found: " + filename);
        }

        return Arrays.copyOf(content, content.length);
    }
}
