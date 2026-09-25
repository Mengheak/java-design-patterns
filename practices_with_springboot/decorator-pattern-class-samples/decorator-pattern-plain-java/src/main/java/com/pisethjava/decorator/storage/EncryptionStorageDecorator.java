package com.pisethjava.decorator.storage;

public final class EncryptionStorageDecorator extends StorageDecorator {

    private static final byte KEY = 0x5A;

    public EncryptionStorageDecorator(StorageService delegate) {
        super(delegate);
    }

    @Override
    public void upload(String filename, byte[] content) {
        delegate.upload(filename, transform(content));
    }

    @Override
    public byte[] download(String filename) {
        return transform(delegate.download(filename));
    }

    private byte[] transform(byte[] content) {
        byte[] result = content.clone();

        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (result[i] ^ KEY);
        }

        return result;
    }
}
