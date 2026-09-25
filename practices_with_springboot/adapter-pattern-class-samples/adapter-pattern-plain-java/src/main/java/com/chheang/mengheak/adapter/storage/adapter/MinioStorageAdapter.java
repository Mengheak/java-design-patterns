package com.chheang.mengheak.adapter.storage.adapter;

import com.chheang.mengheak.adapter.storage.domain.StoredFile;
import com.chheang.mengheak.adapter.storage.external.MinioClient;
import com.chheang.mengheak.adapter.storage.target.StorageService;
import java.util.UUID;

public final class MinioStorageAdapter implements StorageService {
	private final MinioClient client;
	private final String bucket;

	public MinioStorageAdapter(MinioClient c, String b) {
		client = c;
		bucket = b;
	}

	public StoredFile upload(String f, byte[] x) {
		String id = UUID.randomUUID().toString();
		return new StoredFile(id, client.putObject(bucket, id + "-" + f, x));
	}
}
