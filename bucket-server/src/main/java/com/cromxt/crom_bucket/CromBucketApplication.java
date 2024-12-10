package com.cromxt.crom_bucket;

import com.cromxt.crom_bucket.exception.InvalidFilePathException;
import com.cromxt.crom_bucket.exception.NotEnoughSpaceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Logger;

@SpringBootApplication
@Slf4j
public class CromBucketApplication {
	private static final String ROOT_FOLDER = "/root_bucket";

	public static void main(String[] args) {
		SpringApplication.run(CromBucketApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext applicationContext, Environment environment){
		return args -> {
			try{
				String systemAbsolutePath = environment.getProperty("BUCKET_SERVICE.SYSTEM_ABSOLUTE_PATH");

				if(systemAbsolutePath == null){
					throw new InvalidFilePathException("BUCKET_SERVICE.SYSTEM_ABSOLUTE_PATH is not set");
				}

				FileStore fileStore = Files.getFileStore(
						FileSystems.getDefault().getPath(systemAbsolutePath)
				);

				long requiredSpaceInBytes = Long.parseLong(Objects.requireNonNull(environment.getProperty("BUCKET_SERVICE.BUCKET_SPACE_SIZE"))) * 1024 * 1024 * 1024;
				long requiredSpaceInGigabytes = requiredSpaceInBytes/1024/1024/1024;
				if(!(fileStore.getUsableSpace()> (requiredSpaceInBytes+1000))){
					throw new NotEnoughSpaceException("Not enough space");
				}

				File rootFolder = new File(systemAbsolutePath+ROOT_FOLDER);

				if(!rootFolder.exists()){
					boolean result = rootFolder.mkdir();
					if(!result){
						throw new InvalidFilePathException("Unable to create root folder");
					}
				}

				log.info("Allocated {} GB of space.",requiredSpaceInGigabytes);

			}catch (Exception e){
				log.error("{} : {}",e.getCause(), e.getMessage());
				((ConfigurableApplicationContext)applicationContext).close();
			}
		};
	}
}
