package com.pisethjava.adapter.storage.adapter;

import com.pisethjava.adapter.storage.domain.StoredFile;
import com.pisethjava.adapter.storage.external.AmazonS3Client;
import com.pisethjava.adapter.storage.target.StorageService;
import java.util.UUID;

public final class S3StorageAdapter implements StorageService {
	private final AmazonS3Client client;
	private final String bucket;

	public S3StorageAdapter(AmazonS3Client c, String b) {
		client = c;
		bucket = b;
	}

	public StoredFile upload(String f, byte[] x) {
		String id = UUID.randomUUID().toString();
		return new StoredFile(id, client.uploadFile(bucket, id + "-" + f, x));
	}
}
