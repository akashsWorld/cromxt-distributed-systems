package com.cromxt.bucket.service.impl;

import java.io.File;
import java.util.UUID;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.service.FileService;
import com.cromxt.proto.files.MediaMetaData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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
        String fileId = generateName(extension);
        return FileDetails.builder()
                .fileId(fileId)
                .fileSize(metaData.getContentLength())
                .contentType(metaData.getContentType())
                .absolutePath(String.format("%s/file-%s.%s",PATH, fileId,extension))
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

    private String accessUrlBuilder(String bucketDetails,
                                    String fileName,
                                    String extension) {

        return String.format("%s://%s:%s/api/v1/objects/%s.%s",
                "protocol",
                "Hostname",
                "Port",
                fileName,
                extension);
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    @ToString
    public static class FileDetails{
        private String fileId;
        private String contentType;
        private String absolutePath;
        private Long fileSize;

    }



}
