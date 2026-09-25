package com.pisethjava.adapter.storage.external;

public class AmazonS3Client {
	public String uploadFile(String bucket, String key, byte[] bytes) {
		System.out.println("S3 upload " + key);
		return "s3://" + bucket + "/" + key;
	}
}
