package com.pisethjava.decorator.storage;

public interface StorageService {

    void upload(String filename, byte[] content);

    byte[] download(String filename);
}
