package com.pisethjava.decorator.storage;

public final class MetricsStorageDecorator extends StorageDecorator {

    public MetricsStorageDecorator(StorageService delegate) {
        super(delegate);
    }

    @Override
    public void upload(String filename, byte[] content) {
        long start = System.nanoTime();

        try {
            delegate.upload(filename, content);
        } finally {
            System.out.println(
                    "[METRICS] upload.duration="
                            + (System.nanoTime() - start)
                            + "ns"
            );
        }
    }

    @Override
    public byte[] download(String filename) {
        long start = System.nanoTime();

        try {
            return delegate.download(filename);
        } finally {
            System.out.println(
                    "[METRICS] download.duration="
                            + (System.nanoTime() - start)
                            + "ns"
            );
        }
    }
}
