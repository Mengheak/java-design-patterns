package com.pisethjava.decorator.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class CompressionStorageDecorator extends StorageDecorator {

    public CompressionStorageDecorator(StorageService delegate) {
        super(delegate);
    }

    @Override
    public void upload(String filename, byte[] content) {
        byte[] compressed = compress(content);
        System.out.printf(
                "[COMPRESS] original=%d bytes, compressed=%d bytes%n",
                content.length,
                compressed.length
        );
        delegate.upload(filename, compressed);
    }

    @Override
    public byte[] download(String filename) {
        return decompress(delegate.download(filename));
    }

    private byte[] compress(byte[] content) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            try (GZIPOutputStream gzip = new GZIPOutputStream(output)) {
                gzip.write(content);
            }

            return output.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not compress content", exception);
        }
    }

    private byte[] decompress(byte[] content) {
        try (
                ByteArrayInputStream input = new ByteArrayInputStream(content);
                GZIPInputStream gzip = new GZIPInputStream(input);
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {
            gzip.transferTo(output);
            return output.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not decompress content", exception);
        }
    }
}
