package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.service.FileService;
import com.cromxt.proto.files.MediaMetaData;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
@Service
@Getter
public class FileServiceImpl implements FileService {


    private final String PATH;

    public FileServiceImpl(Environment environment) {
        this.PATH = environment.getProperty("BUCKET_CONFIG_STORAGE_PATH", String.class);
        assert PATH != null;
    }

    @Override
    public FileDetails generateFileDetails(MediaMetaData metaData) throws MediaOperationException {
        String extension = metaData.getContentType();
        String fileName = generateName(extension);
        return FileDetails.builder()
                .fileName(fileName)
                .fileSize(metaData.getContentLength())
                .contentType(metaData.getContentType())
                .absolutePath(String.format("%s/file-%s.%s",PATH,fileName,extension))
                .build();
    }

    @Override
    public String getFileAbsolutePath(String metaData) {
        File file = new File(metaData);
        return String.format("%s/file-%s",PATH,metaData);
    }

    private String generateName(String extension){
        String fileName = UUID.randomUUID().toString();
        while(new File(String.format("%s/%s.%s",PATH,fileName,extension)).exists()) fileName = UUID.randomUUID().toString();
        return fileName;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    @ToString
    public static class FileDetails{
        private String fileName;
        private String contentType;
        private String absolutePath;
        private Long fileSize;

    }



}
