package com.chheang.mengheak.adapter.storage.target;

import com.chheang.mengheak.adapter.storage.domain.StoredFile;

/**
 * Target interface expected by MediaService.
 *
 * Local storage, MinIO, AWS S3, and Google Cloud adapters all implement it.
 */
public interface StorageService {

	StoredFile upload(String filename, byte[] content);
}
