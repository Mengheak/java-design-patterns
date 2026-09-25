package com.chheang.mengheak.adapter.storage.adapter;

import com.chheang.mengheak.adapter.storage.domain.StoredFile;
import com.chheang.mengheak.adapter.storage.target.StorageService;
import java.util.UUID;

public final class LocalStorageAdapter implements StorageService {
	public StoredFile upload(String filename, byte[] content) {
		String id = UUID.randomUUID().toString();
		return new StoredFile(id, "/tmp/" + id + "-" + filename);
	}
}
