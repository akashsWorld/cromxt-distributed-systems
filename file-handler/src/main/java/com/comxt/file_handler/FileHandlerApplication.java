package com.comxt.file_handler;

import com.comxt.client.bucket.BucketClient;
import com.comxt.file_handler.repository.BucketsRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Queue;

@SpringBootApplication
public class FileHandlerApplication{

	public static void main(String[] args) {
		SpringApplication.run(FileHandlerApplication.class, args);
	}

}
