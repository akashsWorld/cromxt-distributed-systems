package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.service.FileService;
import com.cromxt.bucket.service.MediaObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;

@Service
@RequiredArgsConstructor
public class MediaObjectServiceImpl implements MediaObjectService {

    private final FileService fileService;
    private final ResourceLoader resourceLoader;
    @Override
    public Flux<DataBuffer> getFile(String mediaId) {

        String fileDetails = fileService.getFileAbsolutePath(mediaId);
        resourceLoader.getResource(fileDetails);

        return null;
    }
}
