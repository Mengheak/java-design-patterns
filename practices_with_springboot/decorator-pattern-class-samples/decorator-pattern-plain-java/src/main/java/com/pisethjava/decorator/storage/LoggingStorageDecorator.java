package com.pisethjava.decorator.storage;

public final class LoggingStorageDecorator extends StorageDecorator {

    public LoggingStorageDecorator(StorageService delegate) {
        super(delegate);
    }

    @Override
    public void upload(String filename, byte[] content) {
        System.out.println("[LOG] Upload: " + filename + ", bytes=" + content.length);
        delegate.upload(filename, content);
    }

    @Override
    public byte[] download(String filename) {
        System.out.println("[LOG] Download: " + filename);
        return delegate.download(filename);
    }
}
