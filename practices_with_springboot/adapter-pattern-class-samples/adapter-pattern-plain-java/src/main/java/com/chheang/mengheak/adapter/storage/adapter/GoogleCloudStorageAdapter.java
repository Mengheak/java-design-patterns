package com.chheang.mengheak.adapter.storage.adapter;

import com.chheang.mengheak.adapter.storage.domain.StoredFile;
import com.chheang.mengheak.adapter.storage.external.GoogleCloudStorageClient;
import com.chheang.mengheak.adapter.storage.target.StorageService;
import java.util.UUID;

public final class GoogleCloudStorageAdapter implements StorageService {
	private final GoogleCloudStorageClient client;
	private final String bucket;

	public GoogleCloudStorageAdapter(GoogleCloudStorageClient c, String b) {
		client = c;
		bucket = b;
	}

	public StoredFile upload(String f, byte[] x) {
		String id = UUID.randomUUID().toString();
		return new StoredFile(id, client.createBlob(bucket, id + "-" + f, x));
	}
}
