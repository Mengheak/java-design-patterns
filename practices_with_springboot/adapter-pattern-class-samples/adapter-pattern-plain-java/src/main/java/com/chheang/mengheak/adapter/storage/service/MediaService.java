package com.chheang.mengheak.adapter.storage.service;

import com.chheang.mengheak.adapter.storage.domain.StoredFile;
import com.chheang.mengheak.adapter.storage.target.StorageService;
import java.nio.charset.StandardCharsets;

public final class MediaService {
	private final StorageService storage;

	public MediaService(StorageService s) {
		storage = s;
	}

	public StoredFile uploadText(String name, String content) {
		return storage.upload(name, content.getBytes(StandardCharsets.UTF_8));
	}
}
