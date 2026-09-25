package com.pisethjava.adapter.storage.external;

public class MinioClient {
	public String putObject(String bucket, String name, byte[] bytes) {
		System.out.println("MinIO upload " + name);
		return "minio://" + bucket + "/" + name;
	}
}
