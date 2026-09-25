package com.pisethjava.decorator.storage;

import java.util.Objects;

public abstract class StorageDecorator implements StorageService {

    protected final StorageService delegate;

    protected StorageDecorator(StorageService delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate is required");
    }
}
