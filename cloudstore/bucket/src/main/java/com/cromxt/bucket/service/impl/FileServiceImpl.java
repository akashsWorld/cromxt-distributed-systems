package com.cromxt.bucket.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
    public FileDetails getFileDetails(MediaMetaData metaData) throws RuntimeException {
//        fixme: This method returns a null value.
        log.info("The file name is {}",metaData.getFileName());
        FileDetails.FileDetailsBuilder fileDetailsBuilder = FileDetails.builder();
        String fileNameWithExtension = generateFileName(metaData.getFileName(),metaData.getContentType());
        generateDirectory(fileDetailsBuilder);
        fileDetailsBuilder.fileName(fileNameWithExtension);
        fileDetailsBuilder.contentType(metaData.getContentType());
        FileDetails fileDetails = fileDetailsBuilder.build();
        log.info(fileDetails.getFileName());
        return fileDetails;
    }

    private void generateDirectory(FileDetails.FileDetailsBuilder fileDetails) {
        File file;
        String fileId;
        String fullPath;
        do{
            fileId = String.format("file-%s", UUID.randomUUID());
            fullPath = String.format("%s/%s",PATH,fileId);
            file = new File(fullPath);
        }while (file.exists());
        fileDetails.fileId(fileId);
        fileDetails.basePath(fullPath);
        if (file.mkdirs()) {
            throw new RuntimeException("Unable to create directory");
        }
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
        @Getter(AccessLevel.NONE)
        private String basePath;

        public String getAbsolutePath(){
            return String.format("%s/%s",basePath,fileName);
        }
    }



}
