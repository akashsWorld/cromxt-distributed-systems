package com.cromxt.bucket.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.proto.files.MediaMetaData;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cromxt.bucket.service.FileService;

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
    public FileDetails getFileDetails(MediaMetaData metaData) throws MediaOperationException {
        String fileNameWithExtension = generateFileName(metaData.getFileName(),metaData.getContentType());
        String fileId = generateId();
        String fileName = metaData.getFileName();
        return FileDetails.builder()
                .fileName(fileName)
                .contentType(metaData.getContentType())
                .fileId(fileId)
                .absolutePath(String.format("%s/%s/%s",PATH,fileId,fileNameWithExtension))
                .build();
    }
    private String generateId() throws MediaOperationException{
        String directoryName = null;
        while (Objects.isNull(directoryName) && !new File(String.format("%s/%s",PATH,directoryName)).exists()){
            directoryName = String.format("file-%s",UUID.randomUUID());
        }
        assert directoryName!= null;

        String fullPath = String.format("%s/%s",PATH,directoryName);

        File newMediaObject = new File(fullPath);

        if(!newMediaObject.mkdirs()){
            throw new MediaOperationException("Some error occurred during create the media directory.");
        }
        return directoryName;
    }
    private String generateFileName(String fileName,String contentType) {
        return String.format("%s.%s",fileName,contentType);
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
        private String fileId;
        private String absolutePath;

        public String getAbsolutePath(){
            assert absolutePath != null;
            return absolutePath;
        }
    }



}
