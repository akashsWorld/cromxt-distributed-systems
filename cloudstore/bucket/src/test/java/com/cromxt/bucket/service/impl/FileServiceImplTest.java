package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.service.FileService;
import com.cromxt.proto.files.MediaMetaData;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class FileServiceImplTest {

    @Autowired
    private FileService fileService;



    @Test
    void shouldCreateAFileWithTheDetailsWithMediaObjectMetaData() {
        MediaMetaData mediaMetaData = MediaMetaData.newBuilder()
                .setFileName("long-file-name")
                .setHlsStatus(true)
                .setContentType("mp4")
                .build();
        FileDetails fileDetails = fileService.getFileDetails(mediaMetaData);

        assertEquals(mediaMetaData.getContentType(),fileDetails.getContentType());
        assertNotNull(fileDetails);
        assertNotNull(fileDetails.getAbsolutePath());
        assertNotNull(fileDetails.getFileName());
        assertNotNull(fileDetails.getFileId());
    }
}