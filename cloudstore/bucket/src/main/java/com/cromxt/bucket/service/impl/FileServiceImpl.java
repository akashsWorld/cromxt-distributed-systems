package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.service.FileService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final String PATH;

    public FileServiceImpl(Environment environment) {
        this.PATH = environment.getProperty("BUCKET_CONFIG_STORAGE_PATH", String.class);
        assert PATH != null;

    }

    @Override
    public String createFilePath(String contentType) throws RuntimeException {
        String filePath =  generateFileName(contentType);
        File file = new File(filePath);
        if(!file.getParentFile().mkdirs()){
            throw new RuntimeException("Unable to create directory");
        }
        return filePath;
    }

    private String generateFileName(String contentType) {
        String fileName = generateUrl(
                String.format("%s-%s",UUID.randomUUID(), UUID.randomUUID()),
                contentType);

        File file = new File(fileName);
        while (file.exists()) {
            file = new File(
                    generateUrl(
                            String.format("%s-%s", UUID.randomUUID(), UUID.randomUUID()),
                            contentType
                    )
            );
        }
        return fileName;
    }

    private String generateUrl(String fileName, String contentType) {
        return String.format("%s/%s.%s", getDirectoryPath(fileName), fileName, contentType);
    }
    private String getDirectoryPath(String fileName) {
        return String.format("%s/folder-%s", PATH, fileName);
    }
}
