package com.pisethjava.adapter.storage.external;

public class GoogleCloudStorageClient {
	public String createBlob(String container, String name, byte[] bytes) {
		System.out.println("GCS upload " + name);
		return "gs://" + container + "/" + name;
	}
}
