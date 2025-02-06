package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.service.FileService;
import com.cromxt.bucket.service.MediaObjectService;
import io.netty.buffer.ByteBufAllocator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaObjectServiceImpl implements MediaObjectService {

    private final FileService fileService;
    private final ResourceLoader resourceLoader;
    @Override
    public Flux<DataBuffer> getFile(String mediaId) {

        String fileDetails = fileService.getFileAbsolutePath(mediaId);


        Resource resource = resourceLoader.getResource("file:"+fileDetails);

        if(!resource.exists()){
            log.error("File not found {}", mediaId);
            return Flux.empty();
        }
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(
                ByteBufAllocator.DEFAULT
        );

        try{
            InputStream inputStream = resource.getInputStream();
            return DataBufferUtils.readInputStream(()->inputStream,nettyDataBufferFactory , 4096);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return Flux.empty();
    }
}
