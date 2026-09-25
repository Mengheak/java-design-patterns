package com.chheang.mengheak.adapter.demo;

import com.chheang.mengheak.adapter.storage.adapter.*;
import com.chheang.mengheak.adapter.storage.external.*;
import com.chheang.mengheak.adapter.storage.service.*;
import com.chheang.mengheak.adapter.storage.target.*;
import java.util.List;

public final class StorageHomeworkSolutionDemo {
	public static void run() {
		System.out.println("\n8. STORAGE HOMEWORK");
		List<StorageService> all = List.of(new LocalStorageAdapter(),
				new MinioStorageAdapter(new MinioClient(), "media"),
				new S3StorageAdapter(new AmazonS3Client(), "media"),
				new GoogleCloudStorageAdapter(new GoogleCloudStorageClient(), "media"));
		all.forEach(s -> System.out.println(new MediaService(s).uploadText("room.txt", "Room Cambodia")));
	}
}
