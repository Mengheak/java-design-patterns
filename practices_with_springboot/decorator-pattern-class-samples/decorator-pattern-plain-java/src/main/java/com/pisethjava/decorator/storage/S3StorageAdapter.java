package com.pisethjava.decorator.storage;

import java.util.Objects;

public final class S3StorageAdapter implements StorageService {

    private final S3Client client;
    private final String bucket;

    public S3StorageAdapter(S3Client client, String bucket) {
        this.client = Objects.requireNonNull(client, "client is required");
        this.bucket = Objects.requireNonNull(bucket, "bucket is required");
    }

    @Override
    public void upload(String filename, byte[] content) {
        client.putObject(bucket, filename, content);
    }

    @Override
    public byte[] download(String filename) {
        return client.getObject(bucket, filename);
    }
}
